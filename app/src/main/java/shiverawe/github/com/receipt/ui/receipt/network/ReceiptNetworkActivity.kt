package shiverawe.github.com.receipt.ui.receipt.network

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_network_receipt.*
import kotlinx.android.synthetic.main.activity_receipt.*
import shiverawe.github.com.receipt.R
import shiverawe.github.com.receipt.entity.Receipt
import shiverawe.github.com.receipt.ui.MainActivity
import shiverawe.github.com.receipt.ui.receipt.*
import java.util.*

const val EXTRA_DATE_RECEIPT = "extra_date_receipt"

class NetworkReceiptActivity : BaseReceiptActivity(), ReceiptNetworkContract.View, View.OnClickListener {
    private var presenter: ReceiptNetworkContract.Presenter? = null
    var receiptIsSaved = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_network_receipt)
        container_wait.visibility = View.VISIBLE
        btn_receipt_back.setOnClickListener(this)
        btn_receipt_share.setOnClickListener(this)
        btn_receipt_save.setOnClickListener(this)

        presenter = ReceiptNetworkPresenter()

        if (fromIntentFilter()) {
            presenter?.setQrData(qrData)
            presenter?.getReceipt()
        } else {
            val integrator = IntentIntegrator(this)
            integrator.setBeepEnabled(false)
            integrator.setPrompt("Наведите камеру на qr code")
            integrator.initiateScan()
        }
    }

    override fun onStart() {
        presenter?.attach(this)
        super.onStart()
    }

    override fun onStop() {
        presenter?.detach()
        super.onStop()
    }

    override fun showReceipt(receipt: Receipt) {
        this.receipt = receipt
        container_wait.visibility = View.GONE
        container_error.visibility = View.GONE
        view_receipt.visibility = View.VISIBLE
        btn_receipt_save.visibility = View.VISIBLE
        setReceipt()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_receipt_back -> {
                onBackPressed()
            }
            R.id.btn_receipt_share -> {
                val sendIntent = Intent()
                sendIntent.action = Intent.ACTION_SEND
                sendIntent.putExtra(Intent.EXTRA_TEXT, getShareString())
                sendIntent.type = "text/plain"
                startActivity(Intent.createChooser(sendIntent, "Отправить чек"))
            }
            R.id.btn_receipt_save -> {
                presenter?.save()
            }
        }
    }

    override fun showError(message: String) {
        container_wait.visibility = View.GONE
        view_receipt.visibility = View.GONE
        btn_receipt_save.visibility = View.GONE
        container_error.visibility = View.VISIBLE
        tv_message_error.text = message
    }

    private fun fromIntentFilter(): Boolean {
        val data = intent.data ?: return false
        val path = data.query
        qrData = path
        return true
    }

    override fun showProgress() {
        container_wait.visibility = View.VISIBLE
        view_receipt.visibility = View.GONE
        btn_receipt_save.visibility = View.GONE
        container_error.visibility = View.GONE
    }

    override fun receiptIsSaved() {
        receiptIsSaved = true
        onBackPressed()
    }

    override fun receiptIsNotSaved() {
        showReceipt(receipt!!)
        Toast.makeText(this, "Произошла ошибка при сохранении", Toast.LENGTH_LONG).show()
    }

    override fun onBackPressed() {
        if (fromIntentFilter()) {
            // if activity opened from link
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        } else {
            // if activity opened from application
            if (receiptIsSaved) {
                val intent = Intent()
                intent.putExtra(EXTRA_DATE_RECEIPT, receipt?.meta?.t)
                setResult(Activity.RESULT_OK, intent)
            } else {
                setResult(Activity.RESULT_CANCELED)
            }
        }
        super.onBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result?.contents != null) {
            qrData = result.contents
            presenter?.setQrData(qrData)
            presenter?.getReceipt()
            return
        }
        finish()
    }

    override fun onDestroy() {
        presenter?.destroy()
        super.onDestroy()
    }

}