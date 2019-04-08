package shiverawe.github.com.receipt.ui.history.month

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_month.*
import shiverawe.github.com.receipt.ui.MainActivity
import shiverawe.github.com.receipt.R
import shiverawe.github.com.receipt.entity.receipt.month.ReceiptMonth_v2
import shiverawe.github.com.receipt.ui.Navigation
import shiverawe.github.com.receipt.ui.history.HistoryFragment
import shiverawe.github.com.receipt.ui.history.month.adapter.MonthAdapter
import kotlin.collections.ArrayList

class MonthFragment : Fragment(), MonthContract.View {
    companion object {
        const val DATE_KEY = "date"
        fun getNewInstance(date: Int): MonthFragment {
            val fragment = MonthFragment()
            val bundle = Bundle()
            bundle.putInt(DATE_KEY, date)
            fragment.arguments = bundle
            return fragment
        }
    }

    lateinit var navigation: Navigation
    private var presenter: MonthContract.Presenter? = null
    private lateinit var adapter: MonthAdapter
    private var receipts: ArrayList<ReceiptMonth_v2> = ArrayList()
    private var totalSum = ""
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        navigation = context as MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dateFrom = arguments!!.getInt(DATE_KEY)
        presenter = MonthPresenter(dateFrom)
        adapter = MonthAdapter { receipt -> navigation.openReceipt(receipt.receiptId) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        presenter?.attach(this)
        return inflater.inflate(R.layout.fragment_month, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        rv_month.adapter = adapter
        rv_month.layoutManager = LinearLayoutManager(context)
        if (userVisibleHint) {
            if (receipts.size == 0) presenter?.getReceiptsData()
            else setReceipts(receipts)
        }
    }

    override fun setReceipts(items: ArrayList<ReceiptMonth_v2>) {
        receipts = items
        adapter.setItems(receipts)
        pb_month.visibility = View.GONE
        tv_month_error_message.visibility = View.GONE
        rv_month.visibility = View.VISIBLE
    }

    override fun setTotalSum(totalSum: String) {
        this.totalSum = totalSum
        parentFragment?.let {
            (it as HistoryFragment).setCurrentSum(totalSum)
        }
    }

    override fun showProgressbar() {
        rv_month.visibility = View.GONE
        tv_month_error_message.visibility = View.GONE
        pb_month.visibility = View.VISIBLE
    }

    override fun showError() {
        pb_month.visibility = View.GONE
        tv_month_error_message.text = "Произошла ошибка"
        tv_month_error_message.visibility = View.VISIBLE
    }

    override fun showEmptyDataMessage() {
        pb_month.visibility = View.GONE
        tv_month_error_message.text = "Нет данных"
        tv_month_error_message.visibility = View.VISIBLE
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            if (receipts.size == 0) presenter?.getReceiptsData()
            setTotalSum(totalSum)
        }
    }

    fun update() {
        showProgressbar()
        receipts.clear()
        totalSum = ""
        presenter?.update()
    }

    override fun onDestroyView() {
        presenter?.detach()
        super.onDestroyView()
    }
}