package shiverawe.github.com.receipt.ui.history.month

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_month.*
import org.koin.android.ext.android.get
import org.koin.core.parameter.parametersOf
import shiverawe.github.com.receipt.ui.MainActivity
import shiverawe.github.com.receipt.R
import shiverawe.github.com.receipt.domain.entity.base.ReceiptHeader
import shiverawe.github.com.receipt.ui.Navigation
import shiverawe.github.com.receipt.ui.history.HistoryFragment
import shiverawe.github.com.receipt.ui.history.month.adapter.MonthAdapter
import kotlin.collections.ArrayList

class MonthFragment : Fragment(), MonthContract.View {
    companion object {
        const val DATE_KEY = "date"
        const val POSITION_KEY = "position"
        fun getNewInstance(date: Long, position: Int): MonthFragment {
            val fragment = MonthFragment()
            val bundle = Bundle()
            bundle.putLong(DATE_KEY, date)
            bundle.putInt(POSITION_KEY, position)
            fragment.arguments = bundle
            return fragment
        }
    }

    lateinit var navigation: Navigation
    private var presenter: MonthContract.Presenter? = null
    private lateinit var adapter: MonthAdapter
    private var receipts: ArrayList<ReceiptHeader> = ArrayList()
    private var totalSum = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)
        navigation = context as MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dateFrom = arguments?.getLong(DATE_KEY) ?: 0L
        presenter = get { parametersOf(dateFrom) }
        adapter = MonthAdapter { receipt -> navigation.openReceipt(receipt.receiptId) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        presenter?.attach(this)
        return inflater.inflate(R.layout.fragment_month, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        rv_month.adapter = adapter
        rv_month.layoutManager = LinearLayoutManager(context)
        swipe_refresh_layout_.setOnRefreshListener {
            presenter?.getReceiptsData()
        }
        if (receipts.size == 0) {
            if (userVisibleHint) {
                presenter?.getReceiptsData()
            }
        } else {
            setReceipts(receipts)
        }
    }

    override fun setReceipts(items: ArrayList<ReceiptHeader>) {
        receipts = items
        adapter.setItems(receipts)
        pb_month.visibility = View.GONE
        tv_month_error_message.visibility = View.GONE
        rv_month.visibility = View.VISIBLE
        swipe_refresh_layout_.isRefreshing = false
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
        swipe_refresh_layout_.isRefreshing = false
    }

    override fun showEmptyDataMessage() {
        pb_month.visibility = View.GONE
        tv_month_error_message.text = "Нет данных"
        tv_month_error_message.visibility = View.VISIBLE
        swipe_refresh_layout_.isRefreshing = false
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            if (receipts.size == 0) {
                presenter?.getReceiptsData()
            } else {
                setReceipts(receipts)
            }
        }
    }

    fun update() {
        showProgressbar()
        receipts.clear()
        totalSum = ""
        presenter?.update()
    }

    fun getSum(): String {
        return totalSum
    }

    override fun onDestroyView() {
        presenter?.detach()
        super.onDestroyView()
    }
}