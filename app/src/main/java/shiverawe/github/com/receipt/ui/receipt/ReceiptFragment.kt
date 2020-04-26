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
import shiverawe.github.com.receipt.domain.entity.dto.Receipt
import shiverawe.github.com.receipt.ui.newreceipt.NewReceiptView
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

    private val containerParent: NewReceiptView?
        get() {
            return when {
                parentFragment is NewReceiptView -> parentFragment as NewReceiptView
                activity is NewReceiptView -> activity as NewReceiptView
                else -> null
            }
        }
    private val baseUrl: String by lazy { getString(R.string.BASE_URL) }

    private val presenter: ReceiptContact.Presenter by inject()
    private var adapter = ProductAdapter()
    private val dateFormatterDate = SimpleDateFormat("dd.MM.yy_HH:mm", Locale("ru"))
    private val dateFormatterDay = DateFormat.getDateInstance(SimpleDateFormat.FULL, Locale("ru"))
    private var receipt: Receipt? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        presenter.attach(this)
        return inflater.inflate(R.layout.fragment_receipt, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        btn_toolbar_receipt_back.setOnClickListener(this)
        btn_toolbar_receipt_share.setOnClickListener(this)
        appbar.addOnOffsetChangedListener(
            AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
                header_collapsed.visibility =
                    if (-verticalOffset == collapsed.height - collapsed.minimumHeight) View.VISIBLE
                    else View.INVISIBLE
            }
        )
        sendRequest()
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
        containerParent?.hideProgress()
        this.receipt = receipt
        header_collapsed.apply {
            titleText = receipt.shop.place
            subtitleText = receipt.shop.sum.floorTwo() + " " + resources.getString(R.string.rubleSymbolJava)
        }
        header_expanded.apply {
            titleText = receipt.shop.place
            subtitleText = receipt.shop.sum.floorTwo() + " " + resources.getString(R.string.rubleSymbolJava)
        }
        val day = dateFormatterDay.format(Date(receipt.shop.date)).split(",")[0].capitalize()
        val date = dateFormatterDate.format(Date(receipt.shop.date)).split("_")[0]
        val time = dateFormatterDate.format(Date(receipt.shop.date)).split("_")[1]
        tv_toolbar_receipt_date.text = "$day, $date"
        tv_toolbar_receipt_time.text = time
        adapter.setProducts(receipt.items)
        rv_receipt.adapter = adapter
    }

    override fun showError(error: Throwable) {
        if (Settings.getDevelopMod(context!!)) {
            val message = try {
                baseUrl + "rest/get?" + arguments?.getString(RECEIPT_OPTIONS_EXTRA) + "\n" +
                    (error as HttpException).response().errorBody()?.string()
            } catch (e: Exception) {
                error.message?: "error"
            }
            containerParent?.onError(message)
        } else {
            containerParent?.onError()
        }
    }

    override fun showProgress() {
        containerParent?.showProgress()
    }

    fun sendRequest() {
        val receiptId = arguments?.getLong(RECEIPT_ID_EXTRA) ?: 0L
        val receiptOptions = arguments?.getString(RECEIPT_OPTIONS_EXTRA) ?: ""
        if (receiptId != 0L) {
            presenter.getReceiptById(receiptId)
        } else {
            presenter.getReceiptByMeta(receiptOptions)
        }
    }

    fun getTime() = receipt?.shop?.date

    override fun onDestroy() {
        presenter.detach()
        super.onDestroy()
    }

    private fun getShareString(): String {
        val url = StringBuilder()
        val date = getDateForShare(receipt!!.shop.date)
        url.appendln("Посмотреть чек по ссылке:")
        url.append("http://receipt.shefer.space/?")
        url.appendln("fn=${receipt!!.meta.fn}&i=${receipt!!.meta.fd}&fp=${receipt!!.meta.fp}&s=${receipt!!.meta.s}&t=$date")
        url.appendln("Магазин: ${receipt!!.shop.place}")
        url.appendln("Дата:    ${tv_toolbar_receipt_date.text}")
        url.appendln("Сумма:   ${receipt!!.shop.sum}")
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

    private fun getDateForShare(date: Long): String {
        val strDate = StringBuilder()
        val shareCalendar = GregorianCalendar(TimeZone.getDefault())
        shareCalendar.time = Date(date)
        val year = shareCalendar.get(Calendar.YEAR)
        var month = (shareCalendar.get(Calendar.MONTH) + 1).toString()
        if (month.length == 1) month = "0$month"
        var day = shareCalendar.get(Calendar.DAY_OF_MONTH).toString()
        if (day.length == 1) day = "0$day"
        var hour = shareCalendar.get(Calendar.HOUR_OF_DAY).toString()
        if (hour.length == 1) hour = "0$hour"
        var minutes = shareCalendar.get(Calendar.MINUTE).toString()
        if (minutes.length == 1) minutes = "0$minutes"
        strDate.append(year)
        strDate.append(month)
        strDate.append(day)
        strDate.append("T")
        strDate.append(hour)
        strDate.append(minutes)
        return strDate.toString()
    }

    companion object {

        const val RECEIPT_TAG = "receipt_fragment"
        const val RECEIPT_ID_EXTRA = "receiptId"
        const val RECEIPT_OPTIONS_EXTRA = "receiptOptions"

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
    }
}