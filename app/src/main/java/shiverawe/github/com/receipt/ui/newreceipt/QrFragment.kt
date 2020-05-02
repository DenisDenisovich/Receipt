package shiverawe.github.com.receipt.ui.newreceipt

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.util.Size
import androidx.fragment.app.Fragment
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.android.synthetic.main.fragment_qr.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import shiverawe.github.com.receipt.R
import shiverawe.github.com.receipt.utils.toast
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class QrFragment : Fragment(R.layout.fragment_qr), View.OnClickListener {

    private val waitingDialog = WaitingDialog(onCancel = DialogInterface.OnClickListener { _, _ ->
        viewMode.OnCancelWaiting()
    })

    private val viewMode: CreateReceiptViewModel by sharedViewModel()
    private val stateObserver = Observer<CreateReceiptState> {
        if (it is QrCodeState) {
            if (it.isWaiting) {
                // show waiting dialog
                waitingDialog.show(childFragmentManager, null)
            } else {
                // hide waiting dialog if he is showed
                if(waitingDialog.isAdded) {
                    waitingDialog.dismiss()
                }
            }
            it.error?.let {
                // show error
                waitingDialog.dismiss()
                toast(R.string.error)
            }
        }
    }

    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var imageAnalyzeExecutor: ExecutorService
    private var cameraControl: CameraControl? = null
    private var cameraInfo: CameraInfo? = null
    private var cameraProvider: ProcessCameraProvider? = null

    private val qrCodeAnalyzer = QrCodeAnalyzer()

    private val torchListener = Observer<Int> {
        val flashIcon = if (it == TorchState.ON) R.drawable.ic_flash_off else R.drawable.ic_flash_on
        btn_flash.setImageResource(flashIcon)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        qrCodeAnalyzer.onQrCodeDataFound = {
            (parentFragment as NewReceiptView).openReceipt(it)
        }
        qrCodeAnalyzer.onQrCodeError = {
            toast(R.string.error)
            (parentFragment as NewReceiptView).onError()
        }
        btn_qr_back.setOnClickListener(this)
        btn_flash.setOnClickListener(this)
        btn_qr_reader_manual.setOnClickListener(this)
        createCamera()
        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        imageAnalyzeExecutor.shutdown()
        cameraProvider?.unbindAll()
        cameraInfo?.torchState?.removeObserver(torchListener)
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_qr_back -> {
                activity?.onBackPressed()
            }
            R.id.btn_flash -> {
                // TODO: Bug - sometimes torch doesn't enable in CameraX (hardware).
                //  Replace CameraX with Camare2
                cameraControl?.enableTorch(cameraInfo?.torchState?.value != TorchState.ON)
            }
            R.id.btn_qr_reader_manual -> {
                (parentFragment as NewReceiptView).openManual()
            }
        }
    }

    private fun createCamera() {
        preview_view.post {
            cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
            cameraProviderFuture.addListener(Runnable {
                cameraProvider = cameraProviderFuture.get()
                bindCamera()
            }, ContextCompat.getMainExecutor(requireContext()))
        }
    }

    // TODO: CameraX library in beta now. Replace CameraX with Camare2
    @SuppressLint("UnsafeExperimentalUsageError")
    private fun bindCamera() {
        val preview: Preview = Preview.Builder().build()
        // set camera preview quality and default flash mode
        val imageCapture = ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .setFlashMode(ImageCapture.FLASH_MODE_OFF)
            .build()

        // Create ImageAnalysis, that provides cameras frames
        val imageAnalysis = ImageAnalysis.Builder()
            .setTargetResolution(Size(preview_view.width, preview_view.height))
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()

        // set camera frame listener
        imageAnalyzeExecutor = Executors.newSingleThreadExecutor()
        imageAnalysis.setAnalyzer(imageAnalyzeExecutor, ImageAnalysis.Analyzer { imageProxy ->
            imageProxy.use { proxy ->
                val image = proxy.image ?: return@use
                qrCodeAnalyzer.setImage(image, proxy.imageInfo.rotationDegrees)
            }
        })

        // set camera side: Front of Back
        val cameraSelector: CameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

        // bind camera
        try {
            val provider = cameraProvider ?: return
            val camera = provider.bindToLifecycle(
                this as LifecycleOwner,
                cameraSelector,
                preview,
                imageCapture,
                imageAnalysis
            )
            cameraControl = camera.cameraControl
            cameraControl?.enableTorch(false)
            cameraInfo = camera.cameraInfo
            cameraInfo?.torchState?.observe(this, torchListener)
            preview.setSurfaceProvider(preview_view.createSurfaceProvider(camera.cameraInfo))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}