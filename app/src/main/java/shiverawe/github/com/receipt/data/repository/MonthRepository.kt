package shiverawe.github.com.receipt.data.repository

import io.reactivex.Single
import shiverawe.github.com.receipt.data.network.entity.report.Report
import shiverawe.github.com.receipt.data.network.entity.report.ReportRequest
import shiverawe.github.com.receipt.entity.Meta
import shiverawe.github.com.receipt.entity.Product
import shiverawe.github.com.receipt.entity.Receipt
import shiverawe.github.com.receipt.entity.Shop
import shiverawe.github.com.receipt.ui.App
import java.lang.Exception
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*
import kotlin.collections.ArrayList

class MonthRepository {
    private val receipts: ArrayList<Receipt?> = ArrayList()
    private var singleMonth: Single<ArrayList<Receipt?>>? = null

    fun getMonthReceipt(reportRequest: ReportRequest): Single<ArrayList<Receipt?>> {
        singleMonth = App.api.getReceiptForMonth(reportRequest)
                .map { it -> map(it)}
        return singleMonth!!
    }

    private fun map(response: ArrayList<Report>?): ArrayList<Receipt?> {
        val date = Date()
        val calendar = GregorianCalendar()
        // if response is empty
        if (response == null || response.size == 0) {
            receipts.clear()
            return receipts
        }
        var report = response
        // filer response
        report = ArrayList(report.filter { it.status != "FAILED" })
        report.sortByDescending { it.date }
        if (report.size == 0){
            receipts.clear()
            return receipts
        }
        date.time = report[0].date!!.toLong() * 1000
        calendar.time = date
        var currentWeekNumber = calendar.get(Calendar.WEEK_OF_MONTH)

        if (report.size >= 2) {
            for (bodyIndex in 0 until report.size - 1) {
                mapReceipt(report[bodyIndex])
                date.time = report[bodyIndex + 1].date!!.toLong() * 1000
                calendar.time = date
                // if receipt with new week. Add separator to list
                if (currentWeekNumber > calendar.get(Calendar.WEEK_OF_MONTH)) {
                    receipts.add(null)
                    currentWeekNumber = calendar.get(Calendar.WEEK_OF_MONTH)
                }
            }
        } else {
            mapReceipt(report[0])
        }
        return receipts
    }

    private fun mapReceipt(report: Report) {
        try {
        val products: ArrayList<Product> = ArrayList()
        report.items.forEach {
            products.add(Product(it.text ?: "", it.price
                    ?: 0.0, it.amount ?: 0.0))
        }
        val shopDate = report.date!!.toLong() * 1000
        val shopPlace = report.place ?: ""
        val shopSum = BigDecimal(report.sum ?: 0.0 / 100).setScale(2, RoundingMode.DOWN).toDouble()
        val fn = report.fn.toString()
        val fp = report.fp.toString()
        val i = report.fd.toString()
        val t = shopDate.toString()
        val meta = Meta( t, fn, i, fp, shopSum)
        receipts.add(Receipt(Shop(shopDate, shopPlace, shopSum.toString() + " р"), meta, ArrayList(products)))
        } catch (e: Exception) {}
    }
}