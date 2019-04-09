package shiverawe.github.com.receipt.ui.newreceipt

import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import kotlinx.android.synthetic.main.fragment_qr.*
import shiverawe.github.com.receipt.R

class QrFragment : Fragment(), View.OnClickListener {
    private var codeScanner: CodeScanner? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_qr, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        startScanCamera()
        btn_qr_reader_manual.setOnClickListener {
            (parentFragment as NewReceiptView).openManual()
            codeScanner?.releaseResources()
        }
        btn_qr_back.setOnClickListener(this)
        btn_qr_autofocus.setOnClickListener(this)
        btn_qr_flash.setOnClickListener(this)
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
        codeScanner?.isAutoFocusEnabled = true
        btn_qr_autofocus.setImageResource(R.drawable.ic_autofocus_enable)
    }

    override fun onPause() {
        codeScanner?.releaseResources()
        super.onPause()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_qr_back -> activity?.onBackPressed()
            R.id.btn_qr_autofocus -> {
                codeScanner?.isAutoFocusEnabled = !(codeScanner?.isAutoFocusEnabled ?: false)
                if (codeScanner?.isAutoFocusEnabled == true) btn_qr_autofocus.setImageResource(R.drawable.ic_autofocus_enable)
                else btn_qr_autofocus.setImageResource(R.drawable.ic_autofocus_disable)
            }
            R.id.btn_qr_flash -> {
                codeScanner?.isFlashEnabled = !(codeScanner?.isFlashEnabled ?: false)
                if (codeScanner?.isFlashEnabled == true) btn_qr_flash.setImageResource(R.drawable.ic_flash_enable)
                else btn_qr_flash.setImageResource(R.drawable.ic_flash_disable)

            }
        }
    }
}