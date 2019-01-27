package shiverawe.github.com.receipt.ui.receipt.local

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_receipt.*
import shiverawe.github.com.receipt.R
import shiverawe.github.com.receipt.entity.Receipt
import java.util.*
import android.view.View
import android.content.Intent
import android.widget.Toast
import shiverawe.github.com.receipt.ui.receipt.BaseReceiptActivity
import shiverawe.github.com.receipt.ui.receipt.RvAdapterReceipt
import java.lang.Exception


const val RECEIPT_TAG = "receipt"
class ReceiptActivity : BaseReceiptActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receipt)
        receipt = Gson().fromJson(intent.getStringExtra(RECEIPT_TAG), Receipt::class.java)
        btn_receipt_back.setOnClickListener(this)
        btn_receipt_share.setOnClickListener(this)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            setShadow()
        }
        fl_receipt_top_ticket.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        try {
            showReceipt()
        }catch (e: Exception) {
            showError()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_receipt_back -> {
                finish()
            }
            R.id.btn_receipt_share -> {
                val sendIntent = Intent()
                sendIntent.action = Intent.ACTION_SEND
                sendIntent.putExtra(Intent.EXTRA_TEXT, getShareString())
                sendIntent.type = "text/plain"
                startActivity(Intent.createChooser(sendIntent, "Отправить чек"))
            }
        }
    }

    private fun showReceipt() {
        val fullDate = dateFormatterDigits.format(Date(receipt!!.shop.date)).split("_")
        val day = dateFormatterDay.format(Date(receipt!!.shop.date)).split(",")[0].capitalize()
        val date = fullDate[0]
        val time = fullDate[1]
        dateStr = "$date $day $time"
        tv_receipt_date.text = dateStr
        tv_receipt_shop_name.text = receipt!!.shop.place
        tv_receipt_shop_price.text = receipt!!.shop.sum
        rv_receipt.adapter = RvAdapterReceipt(receipt!!.items!!)
        rv_receipt.layoutManager = LinearLayoutManager(this)
    }

    private fun showError() {
        Toast.makeText(this, "Ошибка", Toast.LENGTH_LONG).show()
    }
}