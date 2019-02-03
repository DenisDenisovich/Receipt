package shiverawe.github.com.receipt.data.network

import io.reactivex.Single
import shiverawe.github.com.receipt.data.network.entity.report.Report
import shiverawe.github.com.receipt.data.network.entity.report.ReportRequest
import shiverawe.github.com.receipt.entity.Meta
import shiverawe.github.com.receipt.entity.Product
import shiverawe.github.com.receipt.entity.Receipt
import shiverawe.github.com.receipt.entity.Shop
import shiverawe.github.com.receipt.ui.App
import java.lang.Exception
import java.lang.NullPointerException
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*
import kotlin.collections.ArrayList

class MonthNetwork {

    fun getMonthReceipts(reportRequest: ReportRequest): Single<ArrayList<Receipt>> {
        return App.api.getReceiptForMonth(reportRequest)
                .map { it -> UtilsNetwork.mapMonth(it) }
    }

    fun addWeekSeparators(receipts: ArrayList<Receipt>): ArrayList<Receipt?> {
        if (receipts.size == 0) return ArrayList()
        val receiptsWithWeek = ArrayList<Receipt?>()
        val date = Date()
        val calendar = GregorianCalendar()
        date.time = receipts[0].meta.t.toLong()
        calendar.time = date
        var currentWeekNumber = calendar.get(Calendar.WEEK_OF_MONTH)
        if (receipts.size >= 2) {
            for (receiptIndex in 0 until receipts.size) {
                receiptsWithWeek.add(receipts[receiptIndex])
                if (receiptIndex == receipts.lastIndex) break
                date.time = receipts[receiptIndex + 1].meta.t.toLong()
                calendar.time = date
                // if receipt with new week. Add separator to list
                if (currentWeekNumber > calendar.get(Calendar.WEEK_OF_MONTH)) {
                    receiptsWithWeek.add(null)
                    currentWeekNumber = calendar.get(Calendar.WEEK_OF_MONTH)
                }
            }
        } else {
            receiptsWithWeek.add(receipts[0])
        }
        return receiptsWithWeek
    }
}