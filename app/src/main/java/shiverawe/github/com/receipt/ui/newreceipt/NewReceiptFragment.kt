package shiverawe.github.com.receipt.ui.newreceipt

import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import shiverawe.github.com.receipt.R
import shiverawe.github.com.receipt.domain.entity.ErrorType
import shiverawe.github.com.receipt.utils.toast

abstract class NewReceiptFragment(@LayoutRes layoutId: Int) : Fragment(layoutId) {

    protected val waitingDialog = CreateReceiptDialog(onCancel = ::onCancelDialogClick)
    protected var errorToast: Toast? = null

    protected fun showDialog() {
        waitingDialog.showNow(childFragmentManager, null)
    }

    // hide waiting dialog if he is showed
    protected fun dismissDialog() {
        if (waitingDialog.isAdded) {
            waitingDialog.dismiss()
        }
    }

    /**
     * Show error on dialog or on toast
     * @return true if error is shown
     **/
    protected fun showError(errorState: ErrorState): Boolean {
        val message = when {
            errorState.message != null -> errorState.message
            errorState.type == ErrorType.ERROR -> getString(R.string.error)
            errorState.type == ErrorType.OFFLINE -> getString(R.string.error_network)
            else -> getString(R.string.error)
        }

        return when {
            waitingDialog.isAdded -> {
                // if dialog is already showed, show error on dialog
                waitingDialog.setError(message)
                true
            }
            errorToast?.view?.isShown != true -> {
                // if toast isn't show now, show error on toast
                errorToast = toast(message, isLongDuration = false)
                true
            }
            else -> {
                false
            }
        }
    }

    abstract fun onCancelDialogClick()
}