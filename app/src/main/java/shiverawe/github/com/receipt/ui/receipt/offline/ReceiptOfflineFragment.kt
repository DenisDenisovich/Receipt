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
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import shiverawe.github.com.receipt.data.repository.ReceiptRepository
import shiverawe.github.com.receipt.ui.receipt.BaseReceiptFragment
import java.lang.Exception


class ReceiptOfflineFragment : BaseReceiptFragment(), View.OnClickListener {

    companion object {
        const val RECEIPT_ID_TAG = "receiptId"
        fun getNewInstance(receiptId: Long): ReceiptOfflineFragment {
            val fragment = ReceiptOfflineFragment()
            val bundle = Bundle()
            bundle.putLong(RECEIPT_ID_TAG, receiptId)
            fragment.arguments = bundle
            return fragment
        }
    }

    private val repository = ReceiptRepository()
    private var disposable: Disposable? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        disposable = repository.getReceiptById(arguments?.getLong(RECEIPT_ID_TAG)?:0L)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    receipt ->
                    showReceipt(receipt)
                },{
                    _ ->
                    showError()
                })
        return inflater.inflate(R.layout.fragment_receipt, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        btn_receipt_back.setOnClickListener(this)
        btn_receipt_share.setOnClickListener(this)
    }

    private fun showReceipt(receipt: Receipt) {
        this.receipt = receipt
        setReceipt()
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
        Toast.makeText(context, "Произошла ошибка", Toast.LENGTH_LONG).show()
        activity?.onBackPressed()
    }

    override fun onDestroyView() {
        disposable?.dispose()
        super.onDestroyView()
    }
}