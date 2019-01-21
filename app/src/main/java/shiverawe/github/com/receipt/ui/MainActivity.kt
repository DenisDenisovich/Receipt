package shiverawe.github.com.receipt.ui

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import shiverawe.github.com.receipt.R
import shiverawe.github.com.receipt.data.Receipt
import shiverawe.github.com.receipt.ui.history.FragmentHistory
import shiverawe.github.com.receipt.ui.receipt.RECEIPT_TAG
import shiverawe.github.com.receipt.ui.receipt.ReceiptActivity
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.content_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import shiverawe.github.com.receipt.data.Product
import shiverawe.github.com.receipt.data.Shop
import shiverawe.github.com.receipt.data.network.entity.get.ReceiptResponce
import shiverawe.github.com.receipt.ui.receipt.RECEIPT_QR_CODE
import java.lang.Exception
import java.util.*
import kotlin.collections.HashMap


private const val FRAGMENT_HISTORY_TAG = "fragment_history"
const val REQUEST_CODE_CREATE_RECEIPT = 10236
const val EXTRA_DATE_RECEIPT = "extra_date_receipt"
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, Navigation {
    var qrCall: Call<ReceiptResponce>? = null
    var fromReceipt = false
    var qrCode = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        nav_view.setNavigationItemSelectedListener(this)
        openHistory()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_history -> {
                openHistory()
            }

        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onResume() {
        if (fromReceipt) {
            container_qr_request.visibility = View.GONE
            fromReceipt = false
        }
        super.onResume()
    }

    override fun openHistory() {
        supportFragmentManager.beginTransaction().replace(R.id.container, FragmentHistory(), FRAGMENT_HISTORY_TAG).commit()
    }

    override fun openQr() {
        val integrator = IntentIntegrator(this)
        integrator.setBeepEnabled(false)
        integrator.setPrompt("Наведите камеру на qr code")
        integrator.initiateScan()
    }

    override fun openNavigationDrawable() {
        drawer_layout.openDrawer(GravityCompat.START)
    }

    override fun closeNavigationDrawable() {
        drawer_layout.closeDrawer(GravityCompat.START)
    }

    override fun openReceipt(receipt: Receipt) {
        fromReceipt = true
        val intent = Intent(this, ReceiptActivity::class.java)
        intent.putExtra(RECEIPT_TAG, Gson().toJson(receipt))
        startActivity(intent)
    }

    override fun openNetworkReceipt(receipt: Receipt) {
        fromReceipt = true
        val intent = Intent(this, ReceiptActivity::class.java)
        intent.putExtra(RECEIPT_TAG, Gson().toJson(receipt))
        intent.putExtra(RECEIPT_QR_CODE, qrCode)
        startActivityForResult(intent, REQUEST_CODE_CREATE_RECEIPT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CODE_CREATE_RECEIPT -> {
                val date = data?.getLongExtra(EXTRA_DATE_RECEIPT, 0L)?: 0L
                val currentFragment = supportFragmentManager.findFragmentByTag(FRAGMENT_HISTORY_TAG)
                if(currentFragment is FragmentHistory) {
                    currentFragment.updateMonth(date)
                }
            }
            else -> {
                val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
                if (result != null) {
                    if (result.contents != null) {
                        qrCode = result.contents
                        findReceipt()
                    }
                } else {
                    super.onActivityResult(requestCode, resultCode, data)
                }
            }
        }

    }

    private fun findReceipt() {
        container_qr_request.visibility = View.VISIBLE
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
                    container_qr_request.visibility = View.GONE
                    Toast.makeText(this@MainActivity, "Ошибка", Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<ReceiptResponce>, response: Response<ReceiptResponce>) {
                    try {
                        val receipt = map(response)
                        openNetworkReceipt(receipt!!)
                    } catch (e: Exception) {
                        container_qr_request.visibility = View.GONE
                        Toast.makeText(this@MainActivity, "Ошибка", Toast.LENGTH_LONG).show()
                    }
                }

            })
        }catch (e: Exception) {
            container_qr_request.visibility = View.GONE
            Toast.makeText(this@MainActivity, "Ошибка", Toast.LENGTH_LONG).show()
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

    override fun onDestroy() {
        qrCall?.cancel()
        super.onDestroy()
    }
}
