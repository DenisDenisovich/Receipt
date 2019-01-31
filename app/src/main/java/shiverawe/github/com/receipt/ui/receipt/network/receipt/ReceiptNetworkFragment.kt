package shiverawe.github.com.receipt.ui.receipt.network.receipt

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_network_receipt.*
import kotlinx.android.synthetic.main.fragment_receipt.*
import kotlinx.android.synthetic.main.view_error.*
import shiverawe.github.com.receipt.R
import shiverawe.github.com.receipt.entity.Receipt
import shiverawe.github.com.receipt.ui.receipt.BaseReceiptFragment
import shiverawe.github.com.receipt.ui.receipt.offline.ReceiptOfflineFragment
import shiverawe.github.com.receipt.ui.receipt.network.ReceiptNetwork

class ReceiptNetworkFragment: BaseReceiptFragment(),  View.OnClickListener , ReceiptNetworkContract.View {
    companion object {
        const val RECEIPT_DATA_TAG = "receipt"
        fun getNewInstance(qrData: String): ReceiptNetworkFragment {
            val fragment = ReceiptNetworkFragment()
            val bundle = Bundle()
            bundle.putString(RECEIPT_DATA_TAG, qrData)
            fragment.arguments = bundle
            return fragment
        }
    }

    private val presenter: ReceiptNetworkContract.Presenter = ReceiptNetworkPresenter()
    private lateinit var receiptNetwork: ReceiptNetwork

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_network_receipt, container, false)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        receiptNetwork = context as ReceiptNetwork
        presenter.attach(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        presenter.setQrData(arguments?.getString(ReceiptOfflineFragment.RECEIPT_TAG)!!)
        btn_receipt_back.setOnClickListener(this)
        btn_receipt_share.setOnClickListener(this)
        btn_receipt_save.setOnClickListener(this)
        btn_error.setOnClickListener(this)
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
            R.id.btn_receipt_save -> {
                btn_receipt_save.visibility = View.GONE
                pb_receipt_save.visibility = View.VISIBLE
                presenter.save()
            }
            R.id.btn_error -> {
                presenter.getReceipt()
            }
        }
    }


    override fun showReceipt(receipt: Receipt) {
        this.receipt = receipt
        view_receipt.visibility = View.VISIBLE
        container_receipt_save.visibility = View.VISIBLE
        container_wait.visibility = View.GONE
        container_error.visibility = View.GONE
        setReceipt()
    }

    override fun showGetReceiptError() {
        view_receipt.visibility = View.GONE
        container_receipt_save.visibility = View.GONE
        container_wait.visibility = View.GONE
        container_error.visibility = View.VISIBLE
    }

    override fun error() {
        Toast.makeText(context, "Произошла ошибка", Toast.LENGTH_LONG).show()
        activity?.onBackPressed()
    }

    override fun showProgress() {
        view_receipt.visibility = View.GONE
        container_receipt_save.visibility = View.GONE
        container_wait.visibility = View.VISIBLE
        container_error.visibility = View.GONE
    }

    override fun receiptIsSaved() {
        Toast.makeText(context, "Чек сохранен", Toast.LENGTH_LONG).show()
        container_receipt_save.visibility = View.GONE
        receiptNetwork.receiptIsSaved(receipt?.meta?.t?.toLong()?:0L * 1000)
    }

    override fun receiptIsAlreadyExist() {
        Toast.makeText(context, "Чек уже существует", Toast.LENGTH_LONG).show()
        container_receipt_save.visibility = View.GONE
    }

    override fun receiptIsNotSaved() {
        btn_receipt_save.visibility = View.VISIBLE
        pb_receipt_save.visibility = View.GONE
        Toast.makeText(context, "Произошла ошибка", Toast.LENGTH_LONG).show()
    }

    override fun onDetach() {
        super.onDetach()
        presenter.detach()
    }

    override fun onDestroy() {
        presenter.destroy()
        super.onDestroy()
    }
}