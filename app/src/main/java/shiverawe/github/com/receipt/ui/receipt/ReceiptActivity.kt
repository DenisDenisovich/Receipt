package shiverawe.github.com.receipt.ui.receipt

import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_receipt.*
import shiverawe.github.com.receipt.R
import shiverawe.github.com.receipt.data.Receipt
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import android.graphics.Outline
import android.graphics.Rect
import android.view.View
import android.view.ViewOutlineProvider



const val RECEIPT_TAG = "receipt"
class ReceiptActivity : AppCompatActivity() {
    var receipt: Receipt? = null
    private val dateFormatterDigits = SimpleDateFormat("dd.MM_HH:mm", Locale("ru"))
    private val dateFormatterDay = DateFormat.getDateInstance(SimpleDateFormat.FULL, Locale("ru"))
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receipt)
        receipt = Gson().fromJson(intent.getStringExtra(RECEIPT_TAG), Receipt::class.java)

        val date = dateFormatterDigits.format(Date(receipt!!.shop.date)).split("_")
        val day = dateFormatterDay.format(Date(receipt!!.shop.date)).split(",")
        tv_receipt_toolbar_date.text = "${date[0]} ${day[0].capitalize()}"
        tv_receipt_toolbar_time.text = date[1]

        tv_receipt_shop_name.text = receipt!!.shop.place
        tv_receipt_shop_price.text = receipt!!.shop.sum
        rv_receipt.adapter = RvAdapterReceipt(receipt!!.items!!)
        rv_receipt.layoutManager = LinearLayoutManager(this)
        btn_receipt_toolbar_back.setOnClickListener { finish() }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            changeDownShadow()
        }
        fl_receipt_top_ticket.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun changeDownShadow() {
        val viewOutlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                val rect = Rect()
                view?.background?.copyBounds(rect)
                rect.top += resources.getDimensionPixelSize(R.dimen.outline_shadow_receipt_padding_bottom)
                rect.bottom -= resources.getDimensionPixelSize(R.dimen.outline_shadow_receipt_padding_bottom)
                outline.setRect(rect)
            }
        }
        rv_receipt.outlineProvider = viewOutlineProvider
    }
}