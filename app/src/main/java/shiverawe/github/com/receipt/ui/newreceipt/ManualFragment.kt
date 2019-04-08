package shiverawe.github.com.receipt.ui.newreceipt

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_manual_input_receipt.*
import shiverawe.github.com.receipt.R
import shiverawe.github.com.receipt.ui.newreceipt.NewReceiptView
import java.lang.StringBuilder

class ManualFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_manual_input_receipt, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        btn_manual_input.setOnClickListener {
            val fd = et_manual_input_fd.text.toString().trim()
            val fn = et_manual_input_fn.text.toString().trim()
            val fp = et_manual_input_fp.text.toString().trim()
            val s = et_manual_input_sum.text.toString().trim()
            val t = et_manual_input_date.text.toString().trim()
            val receiptData = StringBuilder()
            receiptData.append("t=$t&s=$s&fn=$fn&i=$fd&fp=$fp")
            (parentFragment as NewReceiptView).openReceipt(receiptData.toString())
        }

    }
}