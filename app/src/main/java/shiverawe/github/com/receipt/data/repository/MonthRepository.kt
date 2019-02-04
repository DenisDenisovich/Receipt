package shiverawe.github.com.receipt.data.repository

import io.reactivex.Single
import retrofit2.HttpException
import shiverawe.github.com.receipt.data.bd.ReceiptDatabase
import shiverawe.github.com.receipt.data.network.MonthNetwork
import shiverawe.github.com.receipt.data.network.utils.UtilsNetwork
import shiverawe.github.com.receipt.data.network.entity.report.ReportRequest
import shiverawe.github.com.receipt.entity.Receipt
import java.util.*
import kotlin.collections.ArrayList

class MonthRepository {

    private val network = MonthNetwork()
    private val db = ReceiptDatabase()

    fun getMonthReceipt(reportRequest: ReportRequest): Single<ArrayList<Receipt?>> {
        return network.getMonthReceipts(reportRequest)
                .flatMap { networkReceipts ->
                    val dateFrom = reportRequest.meta.date_from.toLong() * 1000L
                    val dateTo = reportRequest.meta.date_to.toLong() * 1000L
                    db.updateMonthCache(dateFrom, dateTo, networkReceipts)
                }
                .map { it -> addWeekSeparators(it) }
                .onErrorResumeNext {
                    if (it is HttpException || !UtilsNetwork.isOnline()) {
                        val dateFrom = reportRequest.meta.date_from.toLong() * 1000L
                        val dateTo = reportRequest.meta.date_to.toLong() * 1000L
                        db.getReceipts(dateFrom, dateTo)
                                .map { cachedReceipts -> addWeekSeparators(cachedReceipts) }
                    } else {
                        Single.error(it)
                    }
                }
    }

    private fun addWeekSeparators(receipts: ArrayList<Receipt>): ArrayList<Receipt?> {
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