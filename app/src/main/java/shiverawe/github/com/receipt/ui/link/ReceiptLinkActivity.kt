package shiverawe.github.com.receipt.ui.link

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import shiverawe.github.com.receipt.R
import shiverawe.github.com.receipt.ui.receipt.ReceiptFragment

class ReceiptLinkActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receipt_link)

        val receiptId = intent?.data?.query?.toLongOrNull()
        if (receiptId == null) {
            // deeplink format is incorrect. Close activity
            finish()
        } else {
            // open receipt screen
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.container_link_receipt, ReceiptFragment.getNewInstance(receiptId))
                .commit()
        }
    }
}