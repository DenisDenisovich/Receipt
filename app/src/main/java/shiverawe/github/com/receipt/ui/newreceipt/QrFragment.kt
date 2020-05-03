package shiverawe.github.com.receipt.ui.newreceipt

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.util.Size
import androidx.fragment.app.Fragment
import android.view.View
import android.view.WindowManager
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.android.synthetic.main.fragment_qr.*
import org.koin.androidx.viewmodel.ext.android.getSharedViewModel
import shiverawe.github.com.receipt.R
import shiverawe.github.com.receipt.utils.toast
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class QrFragment : Fragment(R.layout.fragment_qr), View.OnClickListener {

    private val viewMode: CreateReceiptViewModel by lazy {
        getSharedViewModel<CreateReceiptViewModel>(from = { requireParentFragment() })
    }
    private val stateObserver = Observer<CreateReceiptState> { state ->
        if (state is QrCodeState) {
            if (state.isWaiting) {
                // show waiting dialog
                if (!waitingDialog.isAdded) {
                    waitingDialog.show(childFragmentManager, null)
                }
            } else {
                // hide waiting dialog if he is showed
                if(waitingDialog.isAdded) {
                    waitingDialog.dismiss()
                }
            }
            state.error?.let {
                // show error
                if(waitingDialog.isAdded) {
                    waitingDialog.dismiss()
                }
                toast(R.string.error)
                viewMode.onShowError()
            }
        }
    }

    private val waitingDialog = WaitingDialog(onCancel = DialogInterface.OnClickListener { _, _ ->
        viewMode.onCancelWaiting()
    })

    private val qrCodeAnalyzer = QrCodeAnalyzer()

    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var imageAnalyzeExecutor: ExecutorService
    private var cameraControl: CameraControl? = null
    private var cameraInfo: CameraInfo? = null
    private var cameraProvider: ProcessCameraProvider? = null
    private val torchListener = Observer<Int> {
        val flashIcon = if (it == TorchState.ON) R.drawable.ic_flash_off else R.drawable.ic_flash_on
        btn_flash.setImageResource(flashIcon)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        qrCodeAnalyzer.onQrCodeDataFound = {
            viewMode.createReceipt(it)
        }
        qrCodeAnalyzer.onQrCodeError = {
            viewMode.showError()
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

    override fun onResume() {
        super.onResume()
        viewMode.state.observe(this, stateObserver)
    }

    override fun onPause() {
        super.onPause()
        viewMode.state.removeObserver(stateObserver)
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
                viewMode.goBack()
            }
            R.id.btn_flash -> {
                // TODO: Bug - sometimes torch doesn't enable in CameraX (hardware).
                //  Replace CameraX with Camare2
                cameraControl?.enableTorch(cameraInfo?.torchState?.value != TorchState.ON)
            }
            R.id.btn_qr_reader_manual -> {
                viewMode.goToManualScreen()
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