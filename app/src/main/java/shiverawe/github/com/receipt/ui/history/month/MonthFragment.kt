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
import shiverawe.github.com.receipt.data.Product
import shiverawe.github.com.receipt.data.Receipt
import shiverawe.github.com.receipt.data.Shop
import shiverawe.github.com.receipt.data.network.entity.get.ReceiptResponce
import shiverawe.github.com.receipt.data.network.entity.report.ReportRequest
import shiverawe.github.com.receipt.ui.Navigation
import java.util.*
import kotlin.collections.ArrayList

class MonthFragment: Fragment() {
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
    var receipts: ArrayList<Receipt?> = ArrayList()
    val date = Date()
    val calendar = GregorianCalendar()
    var date_from: Int = 0
    var date_to: Int = 0
    lateinit var adapter: RvAdapterMonth

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        navigation = context as MainActivity
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        sendRequest()
        return inflater.inflate(R.layout.fragment_month, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }

    private fun sendRequest() {
        date_from = arguments!!.getInt(DATE_KEY)
        date.time = date_from.toLong() * 1000
        calendar.time = date
        calendar.add(Calendar.MONTH, 1)
        date_to = (calendar.timeInMillis / 1000).toInt()
        App.api.getReceiptForMonth(ReportRequest(date_from, date_to)).enqueue(object : Callback<ArrayList<ReceiptResponce>> {
            override fun onFailure(call: Call<ArrayList<ReceiptResponce>>, t: Throwable) {
                pb_month.visibility = View.GONE
                tv_month_error_message.text = "Произошла ошибка"
                tv_month_error_message.visibility  =View.VISIBLE
            }

            override fun onResponse(call: Call<ArrayList<ReceiptResponce>>, response: Response<ArrayList<ReceiptResponce>>) {
                map(response)
                if (receipts.size == 0) {
                    receipts = ArrayList()
                    pb_month.visibility = View.GONE
                    tv_month_error_message.text = "Нет данных"
                    tv_month_error_message.visibility  =View.VISIBLE
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

    private fun map(response: Response<ArrayList<ReceiptResponce>>) {
        if (response.body() == null || response.body()!!.size == 0)
            return
        val body = response.body()!!
        body.sortByDescending { it.meta.date }
        date.time = body[0].meta.date.toLong() * 1000
        calendar.time = date
        var lastWeekNumber = calendar.get(Calendar.WEEK_OF_MONTH)
        for (bodyIndex in 0 until body.size - 1) {
            date.time = body[bodyIndex].meta.date.toLong() * 1000
            calendar.time = date
            if (lastWeekNumber > calendar.get(Calendar.WEEK_OF_MONTH)) {
                receipts?.add(null)
                lastWeekNumber = calendar.get(Calendar.WEEK_OF_MONTH)
            }
            val products: ArrayList<Product> = ArrayList()
            body[bodyIndex].items.forEach { products.add(Product(it.text, it.price, it.amount))}
            receipts?.add(Receipt(Shop(body[bodyIndex].meta.date.toInt(), body[bodyIndex].meta.place, body[bodyIndex].meta.sum), ArrayList(products)))
        }
    }
}