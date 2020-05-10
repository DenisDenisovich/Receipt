package shiverawe.github.com.receipt.ui.receipt

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.fragment_receipt.appbar
import kotlinx.android.synthetic.main.fragment_receipt.btn_toolbar_receipt_back
import kotlinx.android.synthetic.main.fragment_receipt.btn_toolbar_receipt_share
import kotlinx.android.synthetic.main.fragment_receipt.collapsed
import kotlinx.android.synthetic.main.fragment_receipt.header_collapsed
import kotlinx.android.synthetic.main.fragment_receipt.tv_toolbar_receipt_date
import org.koin.androidx.viewmodel.ext.android.viewModel
import shiverawe.github.com.receipt.R
import shiverawe.github.com.receipt.domain.entity.base.Receipt
import shiverawe.github.com.receipt.domain.entity.base.ReceiptHeader
import shiverawe.github.com.receipt.ui.receipt.adapter.ProductAdapter
import shiverawe.github.com.receipt.utils.floorTwo
import java.lang.StringBuilder
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.floor

class ReceiptFragment2 : Fragment(R.layout.fragment_receipt), View.OnClickListener {

    private var adapter = ProductAdapter()
    private val offsetChangedListener = AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
        header_collapsed.visibility =
            if (-verticalOffset == collapsed.height - collapsed.minimumHeight) View.VISIBLE
            else View.INVISIBLE
    }

    private val viewModel: ReceiptViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.receiptData.observe(this, Observer { receipt ->

        })
        viewModel.errorData.observe(this, Observer { errorType ->

        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val receiptId = arguments?.getLong(RECEIPT_ID_EXTRA) ?: 0L
        val receiptHeader = arguments?.getSerializable(RECEIPT_HEADER_EXTRA) as? ReceiptHeader
        if (receiptId != 0L) {
            viewModel.getReceipt(receiptId)
        } else if (receiptHeader != null) {
            viewModel.getReceipt(receiptHeader)
        }

        btn_toolbar_receipt_back.setOnClickListener(this)
        btn_toolbar_receipt_share.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        appbar.addOnOffsetChangedListener(offsetChangedListener)
    }

    override fun onPause() {
        super.onPause()
        appbar.removeOnOffsetChangedListener(offsetChangedListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onClose()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_toolbar_receipt_back -> activity?.onBackPressed()
            R.id.btn_toolbar_receipt_share -> shareReceipt()
        }
    }

    private fun shareReceipt() {
        viewModel.receiptData.value?.let { receipt ->
            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(Intent.EXTRA_TEXT, getShareString(receipt))
            sendIntent.type = "text/plain"
            startActivity(Intent.createChooser(sendIntent, getString(R.string.share_receipt)))
        }
    }

    private fun getShareString(receipt: Receipt): String {
        val shop = receipt.header.shop
        val shareText = StringBuilder()

        // build receipt header
        shareText.appendln(getString(R.string.share_title))
        shareText.append(getString(R.string.share_link, receipt.header.receiptId))
        shareText.appendln(getString(R.string.share_sum, shop.place))
        shareText.appendln(getString(R.string.share_date, tv_toolbar_receipt_date.text))
        shareText.appendln(getString(R.string.share_sum, shop.sum))

        // build receipt's products
        var price: String
        var amountNumber: Double
        var amountString: String
        for (productIndex in receipt.items.indices) {
            shareText.appendln("${productIndex + 1}. ${receipt.items[productIndex].text}")
            amountNumber = BigDecimal(receipt.items[productIndex].amount).setScale(3, RoundingMode.DOWN).toDouble()

            amountString = if (amountNumber == floor(amountNumber)) {
                amountNumber.toInt().toString()
            } else {
                amountNumber.toString()
            }

            shareText.appendln(getString(R.string.share_amount, amountString))
            price = receipt.items[productIndex].price.floorTwo()
            shareText.appendln(getString(R.string.share_price, price))
        }

        return shareText.toString()
    }

    companion object {

        const val RECEIPT_TAG = "receipt_fragment"
        const val RECEIPT_ID_EXTRA = "receiptId"
        const val RECEIPT_HEADER_EXTRA = "receiptHeader"

        fun getNewInstance(receiptId: Long) = ReceiptFragment2().apply {
            arguments = Bundle().apply {
                putLong(RECEIPT_ID_EXTRA, receiptId)
            }
        }

        fun getNewInstance(receiptHeader: ReceiptHeader) = ReceiptFragment2().apply {
            arguments = Bundle().apply {
                putSerializable(RECEIPT_HEADER_EXTRA, receiptHeader)
            }
        }
    }
}