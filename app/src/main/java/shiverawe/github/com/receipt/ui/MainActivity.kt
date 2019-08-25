package shiverawe.github.com.receipt.ui

import android.os.Bundle
import com.google.android.material.navigation.NavigationView
import androidx.fragment.app.Fragment
import androidx.core.view.GravityCompat
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import shiverawe.github.com.receipt.R
import shiverawe.github.com.receipt.ui.history.HistoryFragment
import shiverawe.github.com.receipt.ui.newreceipt.NewReceiptFragment
import shiverawe.github.com.receipt.ui.receipt.ReceiptFragment
import shiverawe.github.com.receipt.ui.settings.SettingsFragment

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, Navigation {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
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
            } else if (fragment is NewReceiptFragment) {
                if (!fragment.onBackPressedIsHandled()) supportFragmentManager.popBackStackImmediate()
            } else {
                super.onBackPressed()
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_history -> openHistory()
            R.id.nav_settings -> openSettings()
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun openHistory() {
        cleanBackStack()
        getTransaction().replace(R.id.container, HistoryFragment(), HistoryFragment.HISTORY_TAG).commit()
    }

    override fun openSettings() {
        cleanBackStack()
        getTransaction().replace(R.id.container, SettingsFragment(), SettingsFragment.SETTINGS_TAG).commit()
    }

    override fun updateHistory(date: Long) {
        supportFragmentManager.popBackStackImmediate()
        val currentFragment = findFragmentByTag(HistoryFragment.HISTORY_TAG)
        if (currentFragment is HistoryFragment) {
            currentFragment.updateMonth(date)
        }
    }

    override fun openQr() {
        getTransaction().replace(R.id.container, NewReceiptFragment()).addToBackStack(null).commit()
    }

    override fun openNavigationDrawable() {
        drawer_layout.openDrawer(GravityCompat.START)
    }

    override fun closeNavigationDrawable() {
        drawer_layout.closeDrawer(GravityCompat.START)
    }

    override fun openReceipt(receiptId: Long) {
        getTransaction().apply {
            replace(
                R.id.container,
                ReceiptFragment.getNewInstance(receiptId),
                ReceiptFragment.RECEIPT_TAG
            )
            addToBackStack(null)
            commit()
        }
    }

    private fun findFragmentByTag(tag: String): Fragment? {
        val fragment = supportFragmentManager.findFragmentByTag(tag)
        return if (fragment?.isVisible == true) fragment else null
    }

    private fun getTopFragment() = supportFragmentManager.findFragmentById(R.id.container)

    private fun getTransaction() = supportFragmentManager.beginTransaction()

    private fun cleanBackStack() {
        for (i in 0 until supportFragmentManager.backStackEntryCount) {
            supportFragmentManager.popBackStackImmediate()
        }
    }
}
