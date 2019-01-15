package shiverawe.github.com.receipt.ui

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import shiverawe.github.com.receipt.R
import shiverawe.github.com.receipt.data.Receipt
import shiverawe.github.com.receipt.ui.history.FragmentHistory
import shiverawe.github.com.receipt.ui.receipt.RECEIPT_TAG
import shiverawe.github.com.receipt.ui.receipt.ReceiptActivity

private const val FRAGMENT_HISTORY_TAG = "fragment_history"
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, Navigation {
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
        Toast.makeText(this, "search", Toast.LENGTH_SHORT).show()
    }

    override fun openNavigationDrawable() {
        drawer_layout.openDrawer(GravityCompat.START)
    }

    override fun closeNavigationDrawable() {
        drawer_layout.closeDrawer(GravityCompat.START)
    }

    override fun openReceipt(receipt: Receipt) {
        Log.d("LogReceipt", receipt.toString())
        val intent = Intent(this, ReceiptActivity::class.java)

        intent.putExtra(RECEIPT_TAG, Gson().toJson(receipt))
        startActivity(intent)
    }
}
