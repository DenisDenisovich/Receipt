package shiverawe.github.com.receipt.ui.receipt.network.datainput

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import kotlinx.android.synthetic.main.fragment_qr_reader.*
import shiverawe.github.com.receipt.R
import shiverawe.github.com.receipt.ui.receipt.network.ReceiptNetwork

private const val MY_CAMERA_REQUEST_CODE = 100
class QrReaderFragment: Fragment() {
    private var codeScanner: CodeScanner? = null
    private lateinit var receiptNetwork: ReceiptNetwork
    override fun onAttach(context: Context?) {
        receiptNetwork = context as ReceiptNetwork
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_qr_reader, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        requestOpenCamera()
        btn_qr_reader_manual.setOnClickListener {
            receiptNetwork.openManualInput()
            codeScanner?.releaseResources()
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner?.startPreview()
    }

    fun startScanCamera() {
        val scannerView = scanner_view
        val activity = requireActivity()
        codeScanner = CodeScanner(activity, scannerView)
        codeScanner?.decodeCallback = DecodeCallback {
            activity.runOnUiThread {
                receiptNetwork.openReceiptFragment(it.text, false)
            }
        }
        codeScanner?.errorCallback = ErrorCallback {
            activity.runOnUiThread {
                Toast.makeText(context, "Произошла ошибка", Toast.LENGTH_LONG).show()
                receiptNetwork.openManualInput()
                codeScanner?.releaseResources()
            }
        }
        codeScanner?.startPreview()
    }


    override fun onPause() {
        codeScanner?.releaseResources()
        super.onPause()
    }

    private fun requestOpenCamera() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.CAMERA), MY_CAMERA_REQUEST_CODE)
            } else {
                startScanCamera()
            }
        } else {
            startScanCamera()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startScanCamera()
        } else {
            receiptNetwork.openManualInput()
        }
    }
}