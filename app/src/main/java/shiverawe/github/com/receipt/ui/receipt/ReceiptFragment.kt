package shiverawe.github.com.receipt.ui.receipt

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.fragment_receipt.*
import org.koin.android.ext.android.inject
import retrofit2.HttpException
import shiverawe.github.com.receipt.R
import shiverawe.github.com.receipt.domain.entity.base.Receipt
import shiverawe.github.com.receipt.domain.entity.base.ReceiptHeader
import shiverawe.github.com.receipt.ui.receipt.adapter.ProductAdapter
import shiverawe.github.com.receipt.utils.Settings
import shiverawe.github.com.receipt.utils.floorTwo
import java.lang.Exception
import java.lang.StringBuilder
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.floor

class ReceiptFragment : Fragment(), ReceiptContact.View, View.OnClickListener {

    private val baseUrl: String by lazy { getString(R.string.BASE_URL) }
    private val presenter: ReceiptContact.Presenter by inject()
    private var adapter = ProductAdapter()
    private val shareDateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    private val dateFormatterDate = SimpleDateFormat("dd.MM.yy_HH:mm", Locale("ru"))
    private val dateFormatterDay = DateFormat.getDateInstance(SimpleDateFormat.FULL, Locale("ru"))
    private var receipt: Receipt? = null

    private val offsetChangedListener = AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
        header_collapsed.visibility =
            if (-verticalOffset == collapsed.height - collapsed.minimumHeight) View.VISIBLE
            else View.INVISIBLE
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        presenter.attach(this)
        return inflater.inflate(R.layout.fragment_receipt, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (adapter.itemCount != 0) {
            pb_receipt.visibility = View.GONE
        }
        btn_toolbar_receipt_back.setOnClickListener(this)
        btn_toolbar_receipt_share.setOnClickListener(this)
        sendRequest()
    }

    override fun onResume() {
        super.onResume()
        appbar.addOnOffsetChangedListener(offsetChangedListener)
    }

    override fun onPause() {
        super.onPause()
        appbar.removeOnOffsetChangedListener(offsetChangedListener)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_toolbar_receipt_back -> activity?.onBackPressed()
            R.id.btn_toolbar_receipt_share -> {
                val sendIntent = Intent()
                sendIntent.action = Intent.ACTION_SEND
                sendIntent.putExtra(Intent.EXTRA_TEXT, getShareString())
                sendIntent.type = "text/plain"
                startActivity(Intent.createChooser(sendIntent, "Отправить чек"))
            }
        }
    }

    @SuppressLint("SetTextI18n", "DefaultLocale")
    override fun showReceipt(receipt: Receipt) {
        this.receipt = receipt
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

    override fun showError(error: Throwable) {
        if (Settings.getDevelopMod(context!!)) {
            val message = try {
                baseUrl + "rest/get?" + arguments?.getString(RECEIPT_OPTIONS_EXTRA) + "\n" +
                    (error as HttpException).response()?.errorBody()?.string()
            } catch (e: Exception) {
                error.message?: "error"
            }
        }
    }

    fun sendRequest() {
        val receiptId = arguments?.getLong(RECEIPT_ID_EXTRA) ?: 0L
        val receiptOptions = arguments?.getString(RECEIPT_OPTIONS_EXTRA) ?: ""
        val receiptHeader = arguments?.getSerializable(RECEIPT_HEADER_EXTRA) as? ReceiptHeader

        when {
            receiptId != 0L -> presenter.getReceiptById(receiptId)
            receiptOptions != "" -> presenter.getReceiptByMeta(receiptOptions)
            receiptHeader != null -> presenter.getReceiptByHeader(receiptHeader)
        }
    }

    override fun onDestroy() {
        presenter.detach()
        super.onDestroy()
    }

    private fun getShareString(): String {
        val shop = receipt?.header?.shop ?: return ""
        val meta = receipt?.header?.meta ?: return ""
        val url = StringBuilder()
        val date = shareDateFormatter.format(shop.date)
        url.appendln("Посмотреть чек по ссылке:")
        url.append("http://receipt.shefer.space/?")
        url.appendln("fn=${meta.fn}&i=${meta.fd}&fp=${meta.fp}&s=${meta.s}&t=$date")
        url.appendln("Магазин: ${shop.place}")
        url.appendln("Дата:    ${tv_toolbar_receipt_date.text}")
        url.appendln("Сумма:   ${shop.sum}")
        var price: String
        var amountNumber: Double
        var amountString: String
        for (productIndex in 0 until receipt!!.items.size) {
            url.appendln("${productIndex + 1}. ${receipt!!.items[productIndex].text}")
            amountNumber = BigDecimal(receipt!!.items[productIndex].amount).setScale(3, RoundingMode.DOWN).toDouble()
            amountString = if (amountNumber == floor(amountNumber)) amountNumber.toInt().toString()
            else amountNumber.toString()
            url.appendln("Кол-во: $amountString")
            price = receipt!!.items[productIndex].price.floorTwo() + " p"
            url.appendln("Цена:   $price")
        }
        return url.toString()
    }

    companion object {

        const val RECEIPT_TAG = "receipt_fragment"
        const val RECEIPT_ID_EXTRA = "receiptId"
        const val RECEIPT_OPTIONS_EXTRA = "receiptOptions"
        const val RECEIPT_HEADER_EXTRA = "receiptHeader"

        fun getNewInstance(receiptId: Long): ReceiptFragment {
            val fragment = ReceiptFragment()
            val bundle = Bundle()
            bundle.putLong(RECEIPT_ID_EXTRA, receiptId)
            fragment.arguments = bundle
            return fragment
        }

        fun getNewInstance(receiptOptions: String): ReceiptFragment {
            val fragment = ReceiptFragment()
            val bundle = Bundle()
            bundle.putString(RECEIPT_OPTIONS_EXTRA, receiptOptions)
            fragment.arguments = bundle
            return fragment
        }

        fun getNewInstance(receiptHeader: ReceiptHeader) = ReceiptFragment().apply {
            arguments = Bundle().apply {
                putSerializable(RECEIPT_HEADER_EXTRA, receiptHeader)
            }
        }
    }
}