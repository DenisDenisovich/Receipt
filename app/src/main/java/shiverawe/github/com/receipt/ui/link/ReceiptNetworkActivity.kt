package shiverawe.github.com.receipt.ui.link

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_receipt_link.*
import kotlinx.android.synthetic.main.view_error.*
import shiverawe.github.com.receipt.R
import shiverawe.github.com.receipt.ui.newreceipt.NewReceiptView
import shiverawe.github.com.receipt.ui.receipt.ReceiptFragment

class ReceiptLinkActivity: NewReceiptView, AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receipt_link)
        val data = intent.data
        val path = data.query
        val qrData = path
        btn_error_change_data.visibility = View.GONE
        btn_error_repeat.setOnClickListener {
            (supportFragmentManager.findFragmentById(R.id.container_link_receipt) as ReceiptFragment).sendRequest()
            container_error.visibility = View.GONE
        }
        container_wait.visibility = View.GONE
        container_error.visibility = View.GONE
        openReceipt(qrData)
    }

    override fun openReceipt(options: String) {
        supportFragmentManager.beginTransaction().replace(R.id.container_link_receipt, ReceiptFragment.getNewInstance(options)).commit()
    }

    override fun openManual() {}

    override fun openQr() {}

    override fun showProgress() {
        container_wait.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        container_wait.visibility = View.GONE
    }

    override fun onError() {
        hideProgress()
        container_error.visibility = View.VISIBLE
    }

    override fun onBackPressedIsHandled() = false
}