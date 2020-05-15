package shiverawe.github.com.receipt.ui.receipt.info

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.Intent.CATEGORY_BROWSABLE
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.fragment_receipt.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import shiverawe.github.com.receipt.R
import shiverawe.github.com.receipt.domain.entity.ErrorType
import shiverawe.github.com.receipt.domain.entity.base.Receipt
import shiverawe.github.com.receipt.domain.entity.base.ReceiptHeader
import shiverawe.github.com.receipt.ui.receipt.info.adapter.ProductAdapter
import shiverawe.github.com.receipt.utils.*
import java.text.SimpleDateFormat
import java.util.*

private const val GEO_URI = "geo:0,0?q="
private const val BROWSER_URI = "https://www.google.ru/maps/search/"

class ReceiptFragment : Fragment(R.layout.fragment_receipt), View.OnClickListener {

    private var adapter = ProductAdapter()

    private val offsetChangedListener = AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
        header_collapsed.visibility =
            if (-verticalOffset == collapsed.height - collapsed.minimumHeight) View.VISIBLE
            else View.INVISIBLE
    }

    private val dateFormatter = SimpleDateFormat("EEEE, dd.MM.yyyy", Locale("ru"))
    private val timeFormatter = SimpleDateFormat("HH:mm", Locale("ru"))

    private val viewModel: ReceiptViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.receiptData.observe(this, Observer { receipt -> setReceipt(receipt) })
        viewModel.errorData.observe(this, Observer { errorType -> setError(errorType) })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadReceipt()
        btn_toolbar_receipt_back.setOnClickListener(this)
        btn_toolbar_receipt_share.setOnClickListener(this)
        btn_toolbar_shop_location.setOnClickListener(this)
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
            R.id.btn_repeat -> repeatReceiptLoading()
            R.id.btn_toolbar_shop_location -> showLocation()
        }
    }

    private fun repeatReceiptLoading() {
        rv_receipt.gone()
        tv_error.gone()
        btn_repeat.gone()
        pb_receipt.visible()
        loadReceipt()
    }

    private fun loadReceipt() {
        val receiptId = arguments?.getLong(RECEIPT_ID_KEY) ?: 0L
        val receiptHeader = arguments?.getSerializable(RECEIPT_HEADER_KEY) as? ReceiptHeader
        if (receiptId != 0L) {
            header_collapsed.titleText = getString(R.string.waiting_receipt_text)
            header_expanded.titleText = getString(R.string.waiting_receipt_text)
            viewModel.loadReceipt(receiptId)
        } else if (receiptHeader != null) {
            viewModel.loadReceipt(receiptHeader)
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

        val shopName = receipt.header.shop.title.ifEmpty { getString(R.string.shop_placeholder) }
        val sum = receipt.header.shop.sum
        val date = receipt.header.shop.date

        if (receipt.header.shop.address.isEmpty()) {
            btn_toolbar_shop_location.setColorFilter(color(R.color.colorGray))
        }

        header_collapsed.apply {
            titleText = shopName
            subtitleText = sum.floorTwo() + " " + resources.getString(R.string.rubleSymbolJava)
        }
        header_expanded.apply {
            titleText = shopName
            subtitleText = sum.floorTwo() + " " + resources.getString(R.string.rubleSymbolJava)
        }

        tv_toolbar_receipt_date.text = dateFormatter.format(date).capitalize()
        tv_toolbar_receipt_time.text = timeFormatter.format(date)

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
            sendIntent.putExtra(Intent.EXTRA_TEXT, viewModel.getSharedReceipt(receipt))
            sendIntent.type = "text/plain"
            startActivity(Intent.createChooser(sendIntent, getString(R.string.share_receipt)))
        }
    }

    private fun showLocation() {
        viewModel.receiptData.value?.let { receipt ->
            if (receipt.header.shop.address.isEmpty()) {
                toast(getString(R.string.show_location_empty_address), false)
            } else {
                val address = receipt.header.shop.address
                val locationIntent = Intent()
                locationIntent.addCategory(CATEGORY_BROWSABLE)
                locationIntent.action = Intent.ACTION_VIEW
                try {
                    locationIntent.data = Uri.parse("$GEO_URI$address")
                    startActivity(locationIntent)
                } catch (e: ActivityNotFoundException) {
                    try {
                        locationIntent.data = Uri.parse("$BROWSER_URI$address")
                        startActivity(locationIntent)
                    } catch (e: ActivityNotFoundException) {
                        toast(getString(R.string.show_location_exception), true)
                    }
                }
            }
        }
    }

    companion object {

        const val RECEIPT_TAG = "receipt_fragment"
        private const val RECEIPT_ID_KEY = "receiptId"
        private const val RECEIPT_HEADER_KEY = "receiptHeader"

        fun getNewInstance(receiptId: Long) = ReceiptFragment().apply {
            arguments = Bundle().apply {
                putLong(RECEIPT_ID_KEY, receiptId)
            }
        }

        fun getNewInstance(receiptHeader: ReceiptHeader) = ReceiptFragment().apply {
            arguments = Bundle().apply {
                putSerializable(RECEIPT_HEADER_KEY, receiptHeader)
            }
        }
    }
}