package shiverawe.github.com.receipt.ui.newreceipt

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import kotlinx.android.synthetic.main.fragment_manual.*
import shiverawe.github.com.receipt.R
import java.lang.StringBuilder

class ManualFragment: Fragment(), View.OnFocusChangeListener {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_manual, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        btn_manual.setOnClickListener {
            val fd = et_manual_fd.text.toString().trim()
            val fn = et_manual_fn.text.toString().trim()
            val fp = et_manual_fp.text.toString().trim()
            val s = et_manual_sum.text.toString().trim()
            val t = et_manual_date.text.toString().trim()
            val receiptData = StringBuilder()
            receiptData.append("t=$t&s=$s&fn=$fn&i=$fd&fp=$fp")
            (parentFragment as NewReceiptView).openReceipt(receiptData.toString())
        }
        btn_manual_back.setOnClickListener {
            activity?.onBackPressed()
        }
        et_manual_fd.onFocusChangeListener = this
        et_manual_fn.onFocusChangeListener = this
        et_manual_fp.onFocusChangeListener = this
        et_manual_sum.onFocusChangeListener = this
        et_manual_date.onFocusChangeListener = this
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        changeEtBackground(et_manual_fd)
        changeEtBackground(et_manual_fn)
        changeEtBackground(et_manual_fp)
        changeEtBackground(et_manual_sum)
        changeEtBackground(et_manual_date)
    }

    private fun changeEtBackground(view: EditText) {
        if (!view.hasFocus() && view.text.toString().isNotEmpty())
            view.setBackgroundResource(R.drawable.et_manual_finish)
        else
            view.setBackgroundResource(R.drawable.et_manual)
    }
}