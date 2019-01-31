package shiverawe.github.com.receipt.ui.history.month

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_month.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import shiverawe.github.com.receipt.ui.App
import shiverawe.github.com.receipt.ui.MainActivity
import shiverawe.github.com.receipt.R
import shiverawe.github.com.receipt.entity.Product
import shiverawe.github.com.receipt.entity.Receipt
import shiverawe.github.com.receipt.entity.Shop
import shiverawe.github.com.receipt.data.network.entity.report.Report
import shiverawe.github.com.receipt.data.network.entity.report.ReportRequest
import shiverawe.github.com.receipt.ui.Navigation
import shiverawe.github.com.receipt.ui.history.FragmentHistory
import java.lang.Exception
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*
import kotlin.collections.ArrayList

class MonthFragment : Fragment(), MonthContract.View {
    companion object {
        val DATE_KEY = "date"
        fun getInstance(date: Int): MonthFragment {
            val fragment = MonthFragment()
            val bundle = Bundle()
            bundle.putInt(DATE_KEY, date)
            fragment.arguments = bundle
            return fragment
        }
    }

    lateinit var navigation: Navigation
    private var receipts: ArrayList<Receipt?> = ArrayList()
    private var totalSum = ""
    private lateinit var adapter: RvAdapterMonth
    private var presenter: MonthContract.Presenter? = null
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        navigation = context as MainActivity
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dateFrom = arguments!!.getInt(DATE_KEY)
        presenter = MonthPresenter(dateFrom)
        if (userVisibleHint)
            presenter?.getReceiptsData()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_month, container, false)
    }

    override fun setReceipts(receipts: ArrayList<Receipt?>) {
        this.receipts = receipts
        rv_month.visibility = View.VISIBLE
        pb_month.visibility = View.GONE
        adapter = RvAdapterMonth(receipts) { receipt -> navigation.openReceipt(receipt) }
        rv_month.adapter = adapter
        rv_month.layoutManager = LinearLayoutManager(context)
        rv_month.visibility = View.VISIBLE
        tv_month_error_message.visibility = View.GONE
        pb_month.visibility = View.GONE
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
            if (receipts.size == 0) {
                presenter?.getReceiptsData()
            } else {
                setTotalSum(totalSum)
            }
        }
    }

    fun update() {
        showProgressbar()
        receipts.clear()
        totalSum = ""
        presenter?.update()
    }

    override fun setTotalSum(totalSum: String) {
        this.totalSum = totalSum
        if (parentFragment != null)
            (parentFragment as FragmentHistory).setMonthSum(totalSum)
    }

    fun getTotalSum() = totalSum

    override fun onStart() {
        presenter?.attach(this)
        super.onStart()
    }

    override fun onStop() {
        presenter?.detach()
        super.onStop()
    }

    override fun onDestroyView() {
        presenter?.destroy()
        super.onDestroyView()
    }
}