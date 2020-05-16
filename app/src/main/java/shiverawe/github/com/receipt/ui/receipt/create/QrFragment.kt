package shiverawe.github.com.receipt.ui.receipt.create

import android.annotation.SuppressLint
import android.graphics.*
import android.os.Bundle
import android.util.Size
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.android.synthetic.main.fragment_qr.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import shiverawe.github.com.receipt.R
import shiverawe.github.com.receipt.domain.entity.base.ReceiptStatus
import shiverawe.github.com.receipt.ui.receipt.create.root.CreateReceiptNavigation
import shiverawe.github.com.receipt.ui.receipt.create.state.CreateReceiptState
import shiverawe.github.com.receipt.ui.receipt.create.state.ErrorState
import shiverawe.github.com.receipt.utils.getBottomNavigationBarHeight
import shiverawe.github.com.receipt.utils.gone
import shiverawe.github.com.receipt.utils.toPixels
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class QrFragment : CreateReceiptFragment(R.layout.fragment_qr), View.OnClickListener {

    private val createReceiptNavigation: CreateReceiptNavigation by lazy {
        requireParentFragment() as CreateReceiptNavigation
    }

    private val viewMode: CreateReceiptViewModel by viewModel()
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
            if (!waitingDialog.isAdded) {
                viewMode.createReceipt(it)
            }
        }
        qrCodeAnalyzer.onQrCodeError = {
            if (!waitingDialog.isAdded) {
                showError(ErrorState())
            }
        }
        btn_qr_back.setOnClickListener(this)
        btn_flash.setOnClickListener(this)
        btn_manual.setOnClickListener(this)
        createCamera()
        setupManualButton()
        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        viewMode.state.observe(this, Observer { handleQrState(it) })
    }

    override fun onCancelDialogClick() {
        viewMode.onCancelWaiting()
    }

    override fun onResume() {
        super.onResume()
        iv_preview.visibility = View.GONE
    }

    override fun onPause() {
        super.onPause()
        getPreview()?.let { iv_preview.setImageBitmap(it) }
        iv_preview.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbindCamera()
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_qr_back -> {
                createReceiptNavigation.goBack()
            }
            R.id.btn_flash -> {
                // TODO: Bug - sometimes torch doesn't enable in CameraX (hardware).
                //  Replace CameraX with Camare2
                cameraControl?.enableTorch(cameraInfo?.torchState?.value != TorchState.ON)
            }
            R.id.btn_manual -> {
                btn_manual.gone()
                createReceiptNavigation.openManual()
            }
        }
    }

    private fun handleQrState(state: CreateReceiptState) {
        when {
            state.isWaiting -> {
                showDialog()
            }

            state.receiptHeader != null -> {
                unbindCamera()
                dismissDialog()
                val header = state.receiptHeader
                if (header.status == ReceiptStatus.LOADED) {
                    createReceiptNavigation.openReceipt(header)
                } else {
                    createReceiptNavigation.finish()
                }
            }

            !state.isWaiting && state.error == null -> {
                dismissDialog()
            }

            state.error != null -> {
                state.error?.getValueFirstTime()?.let { errorState ->
                    showError(errorState)
                }
            }
        }
    }

    private fun setupManualButton() {
        btn_manual.layoutParams = (btn_manual.layoutParams as FrameLayout.LayoutParams).apply {
            val margin = 16.toPixels()
            setMargins(margin, margin, margin, margin + getBottomNavigationBarHeight(requireContext()))
        }
        btn_manual.translationY = 150.toPixels().toFloat()
        btn_manual.animate().apply {
            translationYBy(-150.toPixels().toFloat())
            startDelay = 250
            duration = 250
            start()
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

    private fun unbindCamera() {
        imageAnalyzeExecutor.shutdown()
        cameraProvider?.unbindAll()
        cameraInfo?.torchState?.removeObserver(torchListener)
    }

    // get bitmap from preview view
    private fun getPreview(): Bitmap? {
        return try {
            val bitmap = Bitmap.createBitmap(
                preview_view?.width ?: 0,
                preview_view?.height ?: 0,
                Bitmap.Config.ARGB_8888
            )
            preview_view?.let { preview ->
                val canvas = Canvas(bitmap)
                val bgDrawable = preview.background
                if (bgDrawable != null) {
                    bgDrawable.draw(canvas)
                } else {
                    canvas.drawColor(Color.BLACK)
                }
                preview.draw(canvas)
            }
            return bitmap
        } catch (e: IllegalArgumentException) {
            null
        }
    }
}