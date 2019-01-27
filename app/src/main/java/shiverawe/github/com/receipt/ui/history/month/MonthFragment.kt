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

class MonthFragment : Fragment() {
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
    private var call: Call<ArrayList<Report>>? = null
    private var receipts: ArrayList<Receipt?> = ArrayList()
    private var totalSum = 0.0
    private val date = Date()
    private val calendar = GregorianCalendar()
    private var date_from: Int = 0
    private var date_to: Int = 0
    private lateinit var adapter: RvAdapterMonth

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        navigation = context as MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userVisibleHint = false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_month, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            if (receipts.size == 0)
                sendRequest()
            else
                setTotalSum()
        }
    }

    fun update() {
        pb_month.visibility = View.VISIBLE
        rv_month.visibility = View.GONE
        receipts.clear()
        sendRequest()
    }

    fun sendRequest() {
        date_from = arguments!!.getInt(DATE_KEY)
        date.time = date_from.toLong() * 1000
        calendar.time = date
        calendar.add(Calendar.MONTH, 1)
        date_to = (calendar.timeInMillis / 1000).toInt()
        call = App.api.getReceiptForMonth(ReportRequest(date_from, date_to))
        call?.enqueue(object : Callback<ArrayList<Report>> {
            override fun onFailure(call: Call<ArrayList<Report>>, t: Throwable) {
                if (call.isCanceled) return
                pb_month.visibility = View.GONE
                tv_month_error_message.text = "Произошла ошибка"
                tv_month_error_message.visibility = View.VISIBLE
            }

            override fun onResponse(call: Call<ArrayList<Report>>, response: Response<ArrayList<Report>>) {
                try {
                    map(response)
                } catch (e: Exception) {
                    receipts = ArrayList()
                }
                if (receipts.size == 0) {
                    receipts = ArrayList()
                    pb_month.visibility = View.GONE
                    tv_month_error_message.text = "Нет данных"
                    tv_month_error_message.visibility = View.VISIBLE
                } else {
                    rv_month.visibility = View.VISIBLE
                    pb_month.visibility = View.GONE
                    adapter = RvAdapterMonth(receipts) { receipt -> navigation.openReceipt(receipt) }
                    rv_month.adapter = adapter
                    rv_month.layoutManager = LinearLayoutManager(context)
                }
            }
        })
    }

    private fun map(response: Response<ArrayList<Report>>) {
        if (response.body() == null || response.body()!!.size == 0)
            return
        var body = response.body()!!
        body = ArrayList(body.filter { it.meta.date != null })
        body.sortByDescending { it.meta.date }
        date.time = body[0].meta.date!!.toLong() * 1000
        calendar.time = date
        var currentWeekNumber = calendar.get(Calendar.WEEK_OF_MONTH)
        if (body.size >= 2) {
            for (bodyIndex in 0 until body.size - 1) {
                val products: ArrayList<Product> = ArrayList()
                body[bodyIndex].items.forEach {
                    products.add(Product(it.text ?: "", it.price
                            ?: 0.0, it.amount ?: 0.0))
                }
                val shopDate = body[bodyIndex].meta.date!!.toLong() * 1000
                val shopProvider = body[bodyIndex].meta.provider ?: ""
                val shopSum = BigDecimal(body[bodyIndex].meta.sum ?: 0.0 / 100).setScale(2, RoundingMode.DOWN).toDouble()
                totalSum += shopSum
                val fn = body[0].meta.fn.toString()
                val fp = body[0].meta.fp.toString()
                val i = body[0].meta.fd.toString()
                val t = body[0].meta.date!!.toString()
                receipts.add(Receipt(Shop(shopDate, shopProvider, shopSum.toString() + " р", t, fn, i, fp, shopSum.toString()), ArrayList(products)))
                date.time = body[bodyIndex + 1].meta.date!!.toLong() * 1000
                calendar.time = date
                if (currentWeekNumber > calendar.get(Calendar.WEEK_OF_MONTH)) {
                    receipts.add(null)
                    currentWeekNumber = calendar.get(Calendar.WEEK_OF_MONTH)
                }
            }
        } else {
            val products: ArrayList<Product> = ArrayList()
            body[0].items.forEach {
                products.add(Product(it.text ?: "", it.price
                        ?: 0.0, it.amount ?: 0.0))
            }
            val shopDate = body[0].meta.date!!.toLong() * 1000
            val shopProvider = body[0].meta.provider ?: ""
            val shopSum = BigDecimal(body[0].meta.sum ?: 0.0 / 100).setScale(2, RoundingMode.DOWN).toDouble()
            totalSum = shopSum
            val fn = body[0].meta.fn.toString()
            val fp = body[0].meta.fp.toString()
            val i = body[0].meta.fd.toString()
            val t = body[0].meta.date!!.toString()
            receipts.add(Receipt(Shop(shopDate, shopProvider, shopSum.toString() + " р", t, fn, i, fp, shopSum.toString()), ArrayList(products)))
        }
        setTotalSum()
    }

    private fun setTotalSum() {
        totalSum = BigDecimal(totalSum).setScale(2, RoundingMode.DOWN).toDouble()
        if (parentFragment != null)
            (parentFragment as FragmentHistory).setMonthSum(totalSum.toString() + " р")
    }



    override fun onDestroyView() {
        call?.cancel()
        super.onDestroyView()
    }
}