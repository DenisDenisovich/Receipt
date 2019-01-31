package shiverawe.github.com.receipt.ui.receipt.network

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import shiverawe.github.com.receipt.R
import shiverawe.github.com.receipt.ui.MainActivity
import shiverawe.github.com.receipt.ui.receipt.network.receipt.ReceiptNetworkFragment
import shiverawe.github.com.receipt.ui.receipt.network.datainput.ManualInputFragment
import shiverawe.github.com.receipt.ui.receipt.network.datainput.QrReaderFragment

const val EXTRA_DATE_RECEIPT = "extra_date_receipt"
private const val FRAGMENT_RECEIPT_TAG = "receipt"
private const val FRAGMENT_QR_READER_TAG = "qr_reader"
private const val FRAGMENT_MANUAL_INPUT_TAG = "manual_input"

class NetworkReceiptActivity : AppCompatActivity(), ReceiptNetwork {
    private var qrData = ""
    var savedReceiptDate: Long = 0L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_network_receipt)
        if (fromIntentFilter()) {
            openReceiptFragment(qrData, false)
        } else {
            openQrReader()
        }
    }


    private fun fromIntentFilter(): Boolean {
        val data = intent.data ?: return false
        val path = data.query
        qrData = path
        return true
    }

    override fun openQrReader() {
        supportFragmentManager.beginTransaction().add(R.id.container_network_receipt, QrReaderFragment(), FRAGMENT_QR_READER_TAG).commit()
    }

    override fun openManualInput() {
        supportFragmentManager.beginTransaction().add(R.id.container_network_receipt, ManualInputFragment(), FRAGMENT_MANUAL_INPUT_TAG).addToBackStack(null).commit()
    }

    override fun moveBackToManual() {
        supportFragmentManager.popBackStack()
    }

    override fun openReceiptFragment(qrData: String, isManualInput: Boolean) {
        val qrReader = findFragmentByTag(FRAGMENT_QR_READER_TAG)
        if (qrReader != null)
            supportFragmentManager.beginTransaction().remove(qrReader).commit()

        val transaction = supportFragmentManager.beginTransaction()
        val fragment = ReceiptNetworkFragment.getNewInstance(qrData, isManualInput)
        if (isManualInput) {
            transaction.add(R.id.container_network_receipt, fragment, FRAGMENT_RECEIPT_TAG).addToBackStack(null)
        } else
            transaction.replace(R.id.container_network_receipt, fragment, FRAGMENT_RECEIPT_TAG)
        transaction.commit()
    }

    override fun receiptIsSaved(date: Long) {
        savedReceiptDate = date
    }

    override fun onBackPressed() {
        if (findFragmentByTag(FRAGMENT_RECEIPT_TAG) != null && (findFragmentByTag(FRAGMENT_RECEIPT_TAG) as ReceiptNetworkFragment).isErrorStateInManual) {
            // if we in ReceiptFragment in error state and preview fragment was ManualInputFragment
            moveBackToManual()
        } else if (findFragmentByTag(FRAGMENT_MANUAL_INPUT_TAG) != null && findFragmentByTag(FRAGMENT_QR_READER_TAG) != null) {
            // if we in ManualInputFragment and need go back to QrReader
            supportFragmentManager.popBackStack()
            findFragmentByTag(FRAGMENT_QR_READER_TAG)!!.onResume()
        } else if (fromIntentFilter()) {
            // if activity opened from link
            finish()
        } else {
            if (savedReceiptDate != 0L) {
                val intent = Intent()
                intent.putExtra(EXTRA_DATE_RECEIPT, savedReceiptDate)
                setResult(Activity.RESULT_OK, intent)
                finish()
            } else {
                setResult(Activity.RESULT_CANCELED)
                finish()
            }
        }
    }

    private fun findFragmentByTag(tag: String): Fragment? {
        val fragment = supportFragmentManager.findFragmentByTag(tag)
        return if (fragment?.isVisible == true) fragment else null
    }

}