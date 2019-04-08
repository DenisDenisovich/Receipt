package shiverawe.github.com.receipt.ui.newreceipt

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import kotlinx.android.synthetic.main.fragment_qr_reader.*
import shiverawe.github.com.receipt.R

class QrFragment: Fragment() {
    private var codeScanner: CodeScanner? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_qr_reader, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        startScanCamera()
        btn_qr_reader_manual.setOnClickListener {
            (parentFragment as NewReceiptView).openManual()
            codeScanner?.releaseResources()
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner?.startPreview()
    }


    private fun startScanCamera() {
        val scannerView = scanner_view
        val activity = requireActivity()
        codeScanner = CodeScanner(activity, scannerView)
        codeScanner?.isFlashEnabled
        codeScanner?.decodeCallback = DecodeCallback {
            activity.runOnUiThread {
                (parentFragment as NewReceiptView).openReceipt(it.text)
            }
        }
        codeScanner?.errorCallback = ErrorCallback {
            activity.runOnUiThread {
                Toast.makeText(context, "Произошла ошибка", Toast.LENGTH_LONG).show()
                (parentFragment as NewReceiptView).onError()
                codeScanner?.releaseResources()
            }
        }
        codeScanner?.startPreview()
    }

    override fun onPause() {
        codeScanner?.releaseResources()
        super.onPause()
    }
}