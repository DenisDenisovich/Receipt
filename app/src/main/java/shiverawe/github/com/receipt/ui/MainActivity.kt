package shiverawe.github.com.receipt.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import shiverawe.github.com.receipt.R
import shiverawe.github.com.receipt.ui.history.HistoryFragment
import shiverawe.github.com.receipt.ui.newreceipt.NewReceiptFragment
import shiverawe.github.com.receipt.ui.receipt.network.EXTRA_DATE_RECEIPT
import shiverawe.github.com.receipt.ui.receipt.network.NetworkReceiptActivity
import shiverawe.github.com.receipt.ui.receipt_v2.ReceiptFragment


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
            val fragment = getTopFragment()
            if (fragment is ReceiptFragment) {
                supportFragmentManager.popBackStack()
            } else if (fragment is NewReceiptFragment){
                if(!fragment.onBackPressed()) supportFragmentManager.popBackStackImmediate()
            } else {
                super.onBackPressed()
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_history -> openHistory()
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun openHistory() {
        getTransaction().replace(R.id.container, HistoryFragment(), HistoryFragment.HISTORY_TAG).commit()
    }

    override fun updateHistory(date: Long) {
        supportFragmentManager.popBackStackImmediate()
        val currentFragment = findFragmentByTag(HistoryFragment.HISTORY_TAG)
        if(currentFragment is HistoryFragment) {
            currentFragment.updateMonth(date)
        }
    }

    override fun openQr() {
        getTransaction().replace(R.id.container, NewReceiptFragment()).addToBackStack(null).commit()
/*        val intent = Intent(this, NetworkReceiptActivity::class.java)
        startActivityForResult(intent, REQUEST_CODE_CREATE_RECEIPT)*/
    }

    override fun openNavigationDrawable() {
        drawer_layout.openDrawer(GravityCompat.START)
    }

    override fun closeNavigationDrawable() {
        drawer_layout.closeDrawer(GravityCompat.START)
    }

    override fun openReceipt(receiptId: Long) {
        getTransaction().add(R.id.container, ReceiptFragment.getNewInstance(receiptId), ReceiptFragment.RECEIPT_TAG).addToBackStack(null).commit()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CODE_CREATE_RECEIPT -> {
                if (resultCode != Activity.RESULT_OK) return
                val date = data?.getLongExtra(EXTRA_DATE_RECEIPT, 0L)?: 0L
                val currentFragment = findFragmentByTag(HistoryFragment.HISTORY_TAG)
                if(currentFragment is HistoryFragment) {
                    currentFragment.updateMonth(date)
                }
            }
        }
    }

    private fun findFragmentByTag(tag: String) : Fragment? {
        val fragment = supportFragmentManager.findFragmentByTag(tag)
        return if (fragment?.isVisible == true) fragment else null
    }

    private fun getTopFragment() = supportFragmentManager.findFragmentById(R.id.container)
    private fun getTransaction() = supportFragmentManager.beginTransaction()
}
