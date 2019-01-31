package shiverawe.github.com.receipt.ui.receipt.network.datainput

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_manual_input_receipt.*
import shiverawe.github.com.receipt.R
import shiverawe.github.com.receipt.ui.receipt.network.ReceiptNetwork
import java.lang.StringBuilder

class ManualInputFragment: Fragment() {
    var fd = ""
    var fn = ""
    var fp = ""
    var s = ""
    var t = ""
    private lateinit var receiptNetwork: ReceiptNetwork
    override fun onAttach(context: Context?) {
        receiptNetwork = context as ReceiptNetwork
        super.onAttach(context)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_manual_input_receipt, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        btn_manual_input.setOnClickListener {
            fd = et_manual_input_fd.text.toString().trim()
            fn = et_manual_input_fn.text.toString().trim()
            fp = et_manual_input_fp.text.toString().trim()
            s = et_manual_input_sum.text.toString().trim()
            t = et_manual_input_date.text.toString().trim()
            val receiptData = StringBuilder()
            receiptData.append("t=$t&s=$s&fn=$fn&i=$fd&fp=$fp")
            receiptNetwork.openReceiptFragment(receiptData.toString(), true)
        }
    }


}