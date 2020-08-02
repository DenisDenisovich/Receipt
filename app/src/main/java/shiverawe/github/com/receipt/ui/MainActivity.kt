package shiverawe.github.com.receipt.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import shiverawe.github.com.receipt.R
import shiverawe.github.com.receipt.ui.history.HistoryFragment
import shiverawe.github.com.receipt.ui.login.LoginFragment
import shiverawe.github.com.receipt.ui.receipt.create.root.CreateReceiptRootFragment
import shiverawe.github.com.receipt.ui.receipt.info.ReceiptFragment
import shiverawe.github.com.receipt.ui.settings.SettingsFragment
import shiverawe.github.com.receipt.utils.Storage
import shiverawe.github.com.receipt.utils.gone
import shiverawe.github.com.receipt.utils.visible

class MainActivity : AppCompatActivity(), Navigation, View.OnClickListener {

    private val storage: Storage by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (storage.isLogin) {
            openHistory()
        } else {
            openLogin()
        }

        btn_history.setOnClickListener(this)
        btn_qr.setOnClickListener(this)
        btn_settings.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_history -> {
                openHistory()
            }
            R.id.btn_qr -> {
                openQr()
            }
            R.id.btn_settings -> {
                openSettings()
            }
        }
    }

    override fun onBackPressed() {
        when (val topFragment = getTopFragment()) {
            is ReceiptFragment -> {
                supportFragmentManager.popBackStack()
                showBottomAppBar(true)
            }

            is CreateReceiptRootFragment -> {
                if (topFragment.quitOnBackPressed()) {
                    supportFragmentManager.popBackStack()
                    showBottomAppBar(true)
                }
            }

            else -> {
                super.onBackPressed()
            }
        }
    }

    override fun openLogin() {
        changeCurrentScreen {
            setCustomAnimations(R.anim.slide_up_alpha, R.anim.fade_out)
            replace(R.id.container, LoginFragment())
        }

        bottom_app_bar.gone()
    }

    override fun openHistory() {
        cleanBackStack()
        changeCurrentScreen { replace(R.id.container, HistoryFragment(), HistoryFragment.HISTORY_TAG) }
        bottom_app_bar.visible()
    }

    override fun openSettings() {
        cleanBackStack()
        changeCurrentScreen { replace(R.id.container, SettingsFragment(), SettingsFragment.SETTINGS_TAG) }
    }

    override fun openQr() {
        changeCurrentScreen {
            replace(R.id.container, CreateReceiptRootFragment())
            addToBackStack(null)
        }

        showBottomAppBar(false)
    }

    override fun openReceipt(receiptId: Long) {
        changeCurrentScreen {
            setCustomAnimations(
                R.anim.slide_up_alpha,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.slide_down_alpha
            )

            replace(
                R.id.container,
                ReceiptFragment.getNewInstance(receiptId),
                ReceiptFragment.RECEIPT_TAG
            )

            addToBackStack(null)
        }

        showBottomAppBar(false)
    }

    private fun showBottomAppBar(show: Boolean) {
        if (show) {
            bottom_app_bar.animate().apply {
                translationYBy(-bottom_app_bar.height.toFloat())
                duration = 250
                start()
            }
        } else {
            bottom_app_bar.animate().apply {
                translationYBy(bottom_app_bar.height.toFloat())
                duration = 250
                start()
            }
        }
    }

    private fun changeCurrentScreen(actions: FragmentTransaction.() -> Unit) {
        val transaction = supportFragmentManager.beginTransaction()
        actions(transaction)
        transaction.commit()
    }

    private fun getTopFragment() = supportFragmentManager.findFragmentById(R.id.container)

    private fun cleanBackStack() {
        for (i in 0 until supportFragmentManager.backStackEntryCount) {
            supportFragmentManager.popBackStackImmediate()
        }
    }
}
