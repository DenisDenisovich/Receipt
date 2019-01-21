package shiverawe.github.com.receipt.ui

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.google.gson.Gson
import kotlinx.android.synthetic.main.content_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import shiverawe.github.com.receipt.R
import shiverawe.github.com.receipt.data.Product
import shiverawe.github.com.receipt.data.Receipt
import shiverawe.github.com.receipt.data.Shop
import shiverawe.github.com.receipt.data.network.entity.get.ReceiptResponce
import shiverawe.github.com.receipt.ui.receipt.RECEIPT_QR_CODE
import shiverawe.github.com.receipt.ui.receipt.RECEIPT_TAG
import shiverawe.github.com.receipt.ui.receipt.ReceiptActivity
import java.lang.Exception
import java.util.ArrayList

class OpenLinkActivity: AppCompatActivity() {
    var qrCall: Call<ReceiptResponce>? = null
    var fromReceipt = false
    var qrCode = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_link)
        checkIntentFilter()
    }


    fun openNetworkReceipt(receipt: Receipt) {
        fromReceipt = true
        val intent = Intent(this, ReceiptActivity::class.java)
        intent.putExtra(RECEIPT_TAG, Gson().toJson(receipt))
        intent.putExtra(RECEIPT_QR_CODE, qrCode)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivityForResult(intent, REQUEST_CODE_CREATE_RECEIPT)
    }

    private fun findReceipt() {
        try {
            val parameters = qrCode.split("&")
            val options = HashMap<String, String>()
            parameters.forEach {
                parameter ->
                val key = parameter.substring(0, parameter.indexOf("="))
                val value = parameter.substring(parameter.indexOf("=") + 1, parameter.length)
                options[key] = value
            }
            qrCall = App.api.getReceipt(options)
            qrCall?.enqueue(object : Callback<ReceiptResponce> {
                override fun onFailure(call: Call<ReceiptResponce>, t: Throwable) {
                    if (call.isCanceled) return
                    Toast.makeText(this@OpenLinkActivity, "Ошибка", Toast.LENGTH_LONG).show()
                    close()
                }

                override fun onResponse(call: Call<ReceiptResponce>, response: Response<ReceiptResponce>) {
                    try {
                        val receipt = map(response)
                        openNetworkReceipt(receipt!!)
                    } catch (e: Exception) {
                        Toast.makeText(this@OpenLinkActivity, "Ошибка", Toast.LENGTH_LONG).show()
                        close()
                    }
                }

            })
        }catch (e: Exception) {
            Toast.makeText(this@OpenLinkActivity, "Ошибка", Toast.LENGTH_LONG).show()
            close()
        }

    }


    private fun map(response: Response<ReceiptResponce>): Receipt? {
        if (response.body() == null || response.body()?.meta == null || response.body()?.items == null) return null
        val body = response.body()!!
        val products = ArrayList<Product>()
        body.items!!.forEach {
            products.add(Product(it.text?: "", it.price?: 0.0, it.amount?: 0.0))
        }
        val fn = body.meta!!.fn.toString()
        val fp = body.meta.fp.toString()
        val i = body.meta.fd.toString()
        val t = body.meta.date!!.toString()
        val shop = Shop(body.meta.date.toLong()?: 0L, body.meta.place?: "", body.meta.sum?: "", t, fn,i, fp, body.meta.sum?: "")
        return Receipt(shop, products)
    }

    private fun checkIntentFilter() {
        val data = intent.data ?: return
        val path = data.query
        qrCode = path
        findReceipt()
    }

    override fun onBackPressed() {
        close()
    }

    private fun close() {
        qrCall?.cancel()
        finish()
    }

    override fun onDestroy() {
        qrCall?.cancel()
        super.onDestroy()
    }

}