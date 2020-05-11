package shiverawe.github.com.receipt.ui.receipt.create

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.dialog_create_receipt.*
import shiverawe.github.com.receipt.R

class CreateReceiptDialog(private val onCancel: () -> Unit) : DialogFragment() {

    fun setError(message: String) {
        dialog?.tv_error_create?.text = message
        dialog?.pb_create?.visibility = View.GONE
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = AlertDialog.Builder(requireActivity()).run {
        setTitle(getString(R.string.create_receipt_waiting))
        setView(View.inflate(requireContext(), R.layout.dialog_create_receipt, null))
        setNegativeButton(R.string.cancel) { _, _ ->
            onCancel()
        }
        create()
    }
}