package shiverawe.github.com.receipt.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import shiverawe.github.com.receipt.R
import shiverawe.github.com.receipt.entity.Receipt
import shiverawe.github.com.receipt.ui.history.FragmentHistory
import shiverawe.github.com.receipt.ui.receipt.local.ReceiptLocalFragment
import shiverawe.github.com.receipt.ui.receipt.network.EXTRA_DATE_RECEIPT
import shiverawe.github.com.receipt.ui.receipt.network.NetworkReceiptActivity


private const val FRAGMENT_HISTORY_TAG = "fragment_history"
private const val FRAGMENT_LOCAL_RECEIPT_TAG = "fragment_local_receipt"
const val REQUEST_CODE_CREATE_RECEIPT = 10236
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, Navigation {
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
            if (findFragmentByTag(FRAGMENT_LOCAL_RECEIPT_TAG) != null) {
                supportFragmentManager.popBackStack()
            } else {
                super.onBackPressed()
            }
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
        val intent = Intent(this, NetworkReceiptActivity::class.java)
        startActivityForResult(intent, REQUEST_CODE_CREATE_RECEIPT)
    }

    override fun openNavigationDrawable() {
        drawer_layout.openDrawer(GravityCompat.START)
    }

    override fun closeNavigationDrawable() {
        drawer_layout.closeDrawer(GravityCompat.START)
    }

    override fun openReceipt(receipt: Receipt) {
        supportFragmentManager.beginTransaction().add(R.id.container, ReceiptLocalFragment.getNewInstance(Gson().toJson(receipt)), FRAGMENT_LOCAL_RECEIPT_TAG).addToBackStack(null).commit()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CODE_CREATE_RECEIPT -> {
                if (resultCode != Activity.RESULT_OK) return
                val date = data?.getLongExtra(EXTRA_DATE_RECEIPT, 0L)?: 0L
                val currentFragment = supportFragmentManager.findFragmentByTag(FRAGMENT_HISTORY_TAG)
                if(currentFragment is FragmentHistory) {
                    currentFragment.updateMonth(date)
                }
            }
        }
    }

    private fun findFragmentByTag(tag: String) : Fragment? {
        val fragment = supportFragmentManager.findFragmentByTag(tag)
        return if (fragment?.isVisible == true) fragment else null
    }
}
