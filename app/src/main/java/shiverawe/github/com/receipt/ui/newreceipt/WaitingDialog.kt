package shiverawe.github.com.receipt.ui.newreceipt

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import shiverawe.github.com.receipt.R

class WaitingDialog(private val onCancel: DialogInterface.OnClickListener) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = AlertDialog.Builder(requireActivity()).run {
        setNegativeButton(R.string.cancel, onCancel)
        create()
    }
}