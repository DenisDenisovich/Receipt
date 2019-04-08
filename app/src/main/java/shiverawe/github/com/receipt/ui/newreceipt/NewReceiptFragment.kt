package shiverawe.github.com.receipt.ui.newreceipt

import android.Manifest
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_new_receipt.*
import kotlinx.android.synthetic.main.view_error.*
import shiverawe.github.com.receipt.R
import shiverawe.github.com.receipt.ui.MainActivity
import shiverawe.github.com.receipt.ui.receipt.ReceiptFragment

class NewReceiptFragment : Fragment(), NewReceiptView, View.OnClickListener {

    var permissionDisposable: Disposable? = null
    private lateinit var options: String
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_new_receipt, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        container_wait.visibility = View.GONE
        container_error.visibility = View.GONE
        openQr()
        btn_error_change_data.setOnClickListener(this)
        btn_error_repeat.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_error_change_data -> {
                container_error.visibility = View.GONE
                childFragmentManager.popBackStackImmediate()
            }
            R.id.btn_error_repeat -> {
                (getTopFragment() as ReceiptFragment).sendRequest()
            }
        }
    }

    override fun openReceipt(options: String) {
        this.options = options
        if (childFragmentManager.backStackEntryCount != 0 || getTopFragment() is ManualFragment) {
            getTransaction().replace(R.id.new_receipt_container, ReceiptFragment.getNewInstance(options)).addToBackStack(null).commit()
        } else {
            getTransaction().replace(R.id.new_receipt_container, ReceiptFragment.getNewInstance(options)).commit()
        }
    }

    override fun openManual() {
        getTransaction().replace(R.id.new_receipt_container, ManualFragment()).addToBackStack(null).commit()
    }

    override fun openQr() {
        permissionDisposable = RxPermissions(this)
                .request(Manifest.permission.CAMERA)
                .subscribe({ isGranted ->
                    if (isGranted)
                        getTransaction().replace(R.id.new_receipt_container, QrFragment()).commit()
                    else
                        openManual()
                }, {
                    openManual()
                })
    }

    override fun showProgress() {
        container_wait.visibility = View.VISIBLE
        container_error.visibility = View.GONE
    }

    override fun hideProgress() {
        container_wait.visibility = View.GONE
    }

    override fun onError() {
        container_wait.visibility = View.GONE
        val fragment = getTopFragment()
        if (fragment is QrFragment)
            getTransaction().replace(R.id.new_receipt_container, ManualFragment()).commit()
        else if (fragment is ReceiptFragment) {
            container_error.visibility = View.VISIBLE
        }
    }

    override fun onBackPressedIsHandled(): Boolean {
        val fragment = getTopFragment()
        return if (childFragmentManager.backStackEntryCount == 0) {
            permissionDisposable?.dispose()
            false
        } else {
            when {
                fragment is ReceiptFragment && container_error.visibility == View.VISIBLE -> goBackOnBackPressed()
                fragment is ReceiptFragment && container_error.visibility == View.GONE -> {
                    (activity as MainActivity).updateHistory(fragment.getTime() ?: 0)
                    true
                }
                fragment is ManualFragment -> goBackOnBackPressed()
                else -> {
                    permissionDisposable?.dispose()
                    false
                }
            }
        }
    }

    override fun onDestroy() {
        permissionDisposable?.dispose()
        super.onDestroy()
    }

    private fun goBackOnBackPressed(): Boolean {
        childFragmentManager.popBackStack()
        return true
    }

    private fun getTopFragment() = childFragmentManager.findFragmentById(R.id.new_receipt_container)
    private fun getTransaction() = childFragmentManager.beginTransaction()
}