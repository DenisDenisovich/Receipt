package shiverawe.github.com.receipt.ui

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
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
import java.lang.Exception
import java.util.*


private const val FRAGMENT_HISTORY_TAG = "fragment_history"
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, Navigation {
    var qrCall: Call<ReceiptResponce>? = null
    lateinit var menu: Menu
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
        val intent = Intent(this, ReceiptActivity::class.java)
        intent.putExtra(RECEIPT_TAG, Gson().toJson(receipt))
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "Ошибка", Toast.LENGTH_LONG).show()
            } else {
                findReceipt(ArrayList(result.contents.split("&")))
                Log.d("LogTest", "${result.contents}")
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun findReceipt(str: ArrayList<String>) {
        container.visibility = View.INVISIBLE
        container_qr_request.visibility = View.VISIBLE
        try {
            val fn = str[2].split("=")[1].toLong()
            val i = str[3].split("=")[1].toLong()
            val fp = str[4].split("=")[1].toLong()
            val t = mapDate(str[0].split("=")[1])
            val s = if (str[1].split("=")[1].contains(".")) (str[1].split("=")[1].toDouble() * 100).toLong() else str[1].split("=")[1].toLong()
            qrCall = App.api.getReceipt(fn, i, fp, t, s)
            qrCall?.enqueue(object : Callback<ReceiptResponce> {
                override fun onFailure(call: Call<ReceiptResponce>, t: Throwable) {
                    container.visibility = View.VISIBLE
                    container_qr_request.visibility = View.GONE
                    Toast.makeText(this@MainActivity, "Ошибка", Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<ReceiptResponce>, response: Response<ReceiptResponce>) {
                    container.visibility = View.VISIBLE
                    container_qr_request.visibility = View.GONE
                    try {
                        val receipt = map(response)
                        openReceipt(receipt!!)
                    } catch (e: Exception) {
                        Toast.makeText(this@MainActivity, "Ошибка", Toast.LENGTH_LONG).show()
                        Log.d("LogQr", "ReceiptParsing: ${e.message}")

                    }
                }

            })
        }catch (e: Exception) {
            container.visibility = View.VISIBLE
            container_qr_request.visibility = View.GONE
            Toast.makeText(this@MainActivity, "Ошибка", Toast.LENGTH_LONG).show()
            Log.d("LogQr", "qrParsing: ${e.message}")
        }

    }


    private fun map(response: Response<ReceiptResponce>): Receipt? {
        if (response.body() == null || response.body()?.meta == null || response.body()?.items == null) return null
        val body = response.body()!!
        val products = ArrayList<Product>()
        body.items!!.forEach {
            products.add(Product(it.text?: "", it.price?: 0.0, it.amount?: 0.0))
        }
        val shop = Shop(body.meta!!.date?.toLong()?: 0L, body.meta!!.place?: "", body.meta!!.sum?: "")
        return Receipt(shop, products)
    }

    fun mapDate(date: String): Long {
        val d = date.split("T")[0]
        val time = date.split("T")[1]
        val cal = GregorianCalendar(date.substring(0, 4).toInt(), date.substring(3, 6).toInt(), date.substring(6, d.length).toInt(), time.substring(0, 2).toInt(), time.substring(2, time.length).toInt())
        return cal.timeInMillis / 1000
    }
}
