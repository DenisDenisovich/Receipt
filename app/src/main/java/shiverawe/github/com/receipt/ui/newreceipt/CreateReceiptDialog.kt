package shiverawe.github.com.receipt.ui.newreceipt

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import kotlinx.android.synthetic.main.dialog_create_receipt.*
import shiverawe.github.com.receipt.R

private const val MIN_DELAY_BETWEEN_SHOWING = 500

class CreateReceiptDialog(
    private val minDelayBetweenShowing: Int = MIN_DELAY_BETWEEN_SHOWING,
    private val onCancel: DialogInterface.OnClickListener
) : DialogFragment() {

    private var lastStopTime: Long = 0L

    fun setError(message: String) {
        dialog?.tv_error_create?.text = message
        dialog?.pb_create?.visibility = View.GONE
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = AlertDialog.Builder(requireActivity()).run {
        setTitle("Загрузка чека")
        setView(View.inflate(requireContext(), R.layout.dialog_create_receipt, null))
        setNegativeButton(R.string.cancel, onCancel)
        create()
    }

    override fun onStop() {
        super.onStop()
        lastStopTime = System.currentTimeMillis()
    }

    override fun show(manager: FragmentManager, tag: String?) {
        if (System.currentTimeMillis() - lastStopTime > minDelayBetweenShowing) {
            super.show(manager, tag)
        }
    }
}