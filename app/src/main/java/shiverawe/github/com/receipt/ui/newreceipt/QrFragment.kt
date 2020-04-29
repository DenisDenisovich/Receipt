package shiverawe.github.com.receipt.ui.newreceipt

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Size
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.android.synthetic.main.fragment_qr.*
import shiverawe.github.com.receipt.R
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class QrFragment : Fragment(R.layout.fragment_qr), View.OnClickListener {

    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var imageAnalyzeExecutor: ExecutorService
    private var cameraControl: CameraControl? = null
    private var cameraInfo: CameraInfo? = null
    private var cameraProvider: ProcessCameraProvider? = null
    private val qrCodeAnalyzer = QrCodeAnalyzer()
    private val torchListener = Observer<Int> {
        if (it == TorchState.ON) {
            btn_flash.setImageResource(R.drawable.ic_flash_enable)
        } else {
            btn_flash.setImageResource(R.drawable.ic_flash_disable)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        qrCodeAnalyzer.onQrCodeDataFound = {
            (parentFragment as NewReceiptView).openReceipt(it)
        }
        qrCodeAnalyzer.onQrCodeError = {
            Toast.makeText(context, "Произошла ошибка", Toast.LENGTH_LONG).show()
            (parentFragment as NewReceiptView).onError()
        }
        btn_qr_back.setOnClickListener(this)
        btn_flash.setOnClickListener(this)
        btn_qr_reader_manual.setOnClickListener(this)
        setupCamera()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        imageAnalyzeExecutor.shutdown()
        cameraProvider?.unbindAll()
        cameraInfo?.torchState?.removeObserver(torchListener)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_qr_back -> activity?.onBackPressed()
            R.id.btn_flash -> {
                cameraControl?.enableTorch(cameraInfo?.torchState?.value != TorchState.ON)
            }
            R.id.btn_qr_reader_manual -> {
                (parentFragment as NewReceiptView).openManual()
            }
        }
    }

    private fun setupCamera() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener(Runnable {
            preview_view.post {
                cameraProvider = cameraProviderFuture.get()
                bindPreview()
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    @SuppressLint("UnsafeExperimentalUsageError")
    private fun bindPreview() {
        val preview: Preview = Preview.Builder().build()
        val imageCapture = ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .setFlashMode(ImageCapture.FLASH_MODE_OFF)
            .build()
        val imageAnalysis = ImageAnalysis.Builder()
            .setTargetResolution(Size(preview_view.width, preview_view.height))
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()

        // set camera frame listener
        imageAnalyzeExecutor = Executors.newSingleThreadExecutor()
        imageAnalysis.setAnalyzer(imageAnalyzeExecutor, ImageAnalysis.Analyzer { image ->
            image.image?.let { analyzedImage ->
                qrCodeAnalyzer.setImage(analyzedImage, image.imageInfo.rotationDegrees)
            }
            image.close()
        })

        val cameraSelector: CameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

        try {
            cameraProvider?.let { provider ->
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
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}