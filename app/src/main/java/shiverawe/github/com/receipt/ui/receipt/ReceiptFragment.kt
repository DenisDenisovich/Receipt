package shiverawe.github.com.receipt.ui.receipt

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.fragment_receipt.appbar
import kotlinx.android.synthetic.main.fragment_receipt.btn_repeat
import kotlinx.android.synthetic.main.fragment_receipt.btn_toolbar_receipt_back
import kotlinx.android.synthetic.main.fragment_receipt.btn_toolbar_receipt_share
import kotlinx.android.synthetic.main.fragment_receipt.collapsed
import kotlinx.android.synthetic.main.fragment_receipt.header_collapsed
import kotlinx.android.synthetic.main.fragment_receipt.header_expanded
import kotlinx.android.synthetic.main.fragment_receipt.pb_receipt
import kotlinx.android.synthetic.main.fragment_receipt.rv_receipt
import kotlinx.android.synthetic.main.fragment_receipt.tv_error
import kotlinx.android.synthetic.main.fragment_receipt.tv_toolbar_receipt_date
import kotlinx.android.synthetic.main.fragment_receipt.tv_toolbar_receipt_time
import org.koin.androidx.viewmodel.ext.android.viewModel
import shiverawe.github.com.receipt.R
import shiverawe.github.com.receipt.domain.entity.ErrorType
import shiverawe.github.com.receipt.domain.entity.base.Receipt
import shiverawe.github.com.receipt.domain.entity.base.ReceiptHeader
import shiverawe.github.com.receipt.ui.receipt.adapter.ProductAdapter
import shiverawe.github.com.receipt.utils.floorTwo
import shiverawe.github.com.receipt.utils.gone
import shiverawe.github.com.receipt.utils.visible
import java.lang.StringBuilder
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.floor

class ReceiptFragment : Fragment(R.layout.fragment_receipt), View.OnClickListener {

    private var adapter = ProductAdapter()
    private val offsetChangedListener = AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
        header_collapsed.visibility =
            if (-verticalOffset == collapsed.height - collapsed.minimumHeight) View.VISIBLE
            else View.INVISIBLE
    }
    private val dateFormatterDate = SimpleDateFormat("dd.MM.yy_HH:mm", Locale("ru"))
    private val dateFormatterDay = DateFormat.getDateInstance(SimpleDateFormat.FULL, Locale("ru"))

    private val viewModel: ReceiptViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.receiptData.observe(this, Observer { receipt -> setReceipt(receipt)})
        viewModel.errorData.observe(this, Observer { errorType -> setError(errorType) })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getReceipt()

        btn_toolbar_receipt_back.setOnClickListener(this)
        btn_toolbar_receipt_share.setOnClickListener(this)
        btn_repeat.setOnClickListener(this)
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
            R.id.btn_repeat -> repeatReceipt()
        }
    }

    private fun repeatReceipt() {
        rv_receipt.gone()
        tv_error.gone()
        btn_repeat.gone()
        pb_receipt.visible()
        getReceipt()
    }

    private fun getReceipt() {
        val receiptId = arguments?.getLong(RECEIPT_ID_EXTRA) ?: 0L
        val receiptHeader = arguments?.getSerializable(RECEIPT_HEADER_EXTRA) as? ReceiptHeader
        if (receiptId != 0L) {
            viewModel.getReceipt(receiptId)
        } else if (receiptHeader != null) {
            viewModel.getReceipt(receiptHeader)
        }
    }

    @SuppressLint("DefaultLocale", "SetTextI18n")
    private fun setReceipt(receipt: Receipt) {
        rv_receipt.visible()

        if (receipt.items.isEmpty()) {
            pb_receipt.visible()
        } else {
            pb_receipt.gone()
        }

        tv_error.gone()
        btn_repeat.gone()

        val receiptPlace = receipt.header.shop.place
        val receiptSum = receipt.header.shop.sum
        val receiptDate = receipt.header.shop.date

        header_collapsed.apply {
            titleText = receiptPlace
            subtitleText = receiptSum.floorTwo() + " " + resources.getString(R.string.rubleSymbolJava)
        }
        header_expanded.apply {
            titleText = receiptPlace
            subtitleText = receiptSum.floorTwo() + " " + resources.getString(R.string.rubleSymbolJava)
        }

        val day = dateFormatterDay.format(Date(receiptDate)).split(",")[0].capitalize()
        val date = dateFormatterDate.format(Date(receiptDate)).split("_")[0]
        val time = dateFormatterDate.format(Date(receiptDate)).split("_")[1]
        tv_toolbar_receipt_date.text = "$day, $date"
        tv_toolbar_receipt_time.text = time

        if (receipt.items.isNotEmpty()) {
            pb_receipt.visibility = View.GONE
            adapter.setProducts(receipt.items)
        }

        rv_receipt.adapter = adapter
    }

    private fun setError(errorType: ErrorType) {
        rv_receipt.gone()
        pb_receipt.gone()

        if (errorType == ErrorType.ERROR) {
            tv_error.setText(R.string.error)
        } else if (errorType == ErrorType.OFFLINE) {
            tv_error.setText(R.string.error_network)
        }

        btn_repeat.visible()
        tv_error.visible()
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
        shareText.appendln(getString(R.string.share_link, receipt.header.receiptId))
        shareText.appendln(getString(R.string.share_shop, shop.place))
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

        fun getNewInstance(receiptId: Long) = ReceiptFragment().apply {
            arguments = Bundle().apply {
                putLong(RECEIPT_ID_EXTRA, receiptId)
            }
        }

        fun getNewInstance(receiptHeader: ReceiptHeader) = ReceiptFragment().apply {
            arguments = Bundle().apply {
                putSerializable(RECEIPT_HEADER_EXTRA, receiptHeader)
            }
        }
    }
}