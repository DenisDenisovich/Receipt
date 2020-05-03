package shiverawe.github.com.receipt.ui.newreceipt

import android.media.Image
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata
import java.lang.Exception

class QrCodeAnalyzer {

    var onQrCodeDataFound: ((data: String) -> Unit)? = null
    var onQrCodeError: ((e: Exception) -> Unit)? = null

    private val detectorOptions = FirebaseVisionBarcodeDetectorOptions.Builder()
        .setBarcodeFormats(
            FirebaseVisionBarcode.FORMAT_QR_CODE,
            FirebaseVisionBarcode.FORMAT_AZTEC)
        .build()

    private val detector = FirebaseVision.getInstance().getVisionBarcodeDetector(detectorOptions)

    fun setImage(image: Image, degree: Int) {
        val imageRotation = when (degree) {
            0 -> FirebaseVisionImageMetadata.ROTATION_0
            90 -> FirebaseVisionImageMetadata.ROTATION_90
            180 -> FirebaseVisionImageMetadata.ROTATION_180
            270 -> FirebaseVisionImageMetadata.ROTATION_270
            else -> FirebaseVisionImageMetadata.ROTATION_0
        }
        // TODO: FirebaseVisionImage.fromMediaImage throw java.lang.IllegalStateException: buffer is inaccessible.
        //  Find reason this Exception
        try {
            val firebaseImage = FirebaseVisionImage.fromMediaImage(image, imageRotation)
            // set qr code detection listener
            detector.detectInImage(firebaseImage)
                .addOnSuccessListener { barcodes ->
                    barcodes.forEach {
                        it.rawValue?.let { data ->
                            onQrCodeDataFound?.invoke(data)
                        }
                    }
                }
                .addOnFailureListener {
                    onQrCodeError?.invoke(it)
                }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}