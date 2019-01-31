package shiverawe.github.com.receipt.ui.receipt.offline

import android.os.Bundle
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_receipt.*
import shiverawe.github.com.receipt.R
import shiverawe.github.com.receipt.entity.Receipt
import android.view.View
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import shiverawe.github.com.receipt.ui.receipt.BaseReceiptFragment
import java.lang.Exception


class ReceiptOfflineFragment : BaseReceiptFragment(), View.OnClickListener {

    companion object {
        const val RECEIPT_TAG = "receipt"
        fun getNewInstance(receipt: String): ReceiptOfflineFragment {
            val fragment = ReceiptOfflineFragment()
            val bundle = Bundle()
            bundle.putString(RECEIPT_TAG, receipt)
            fragment.arguments = bundle
            return fragment
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_receipt, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        receipt = Gson().fromJson(arguments?.getString(RECEIPT_TAG), Receipt::class.java)
        btn_receipt_back.setOnClickListener(this)
        btn_receipt_share.setOnClickListener(this)
        try {
            setReceipt()
        }catch (e: Exception) {
            showError()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_receipt_back -> {
                activity?.onBackPressed()
            }
            R.id.btn_receipt_share -> {
                val sendIntent = Intent()
                sendIntent.action = Intent.ACTION_SEND
                sendIntent.putExtra(Intent.EXTRA_TEXT, getShareString())
                sendIntent.type = "text/plain"
                startActivity(Intent.createChooser(sendIntent, "Отправить чек"))
            }
        }
    }

    private fun showError() {
        Toast.makeText(context, "Ошибка", Toast.LENGTH_LONG).show()
    }
}