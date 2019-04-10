package shiverawe.github.com.receipt.data.repository

import io.reactivex.Single
import retrofit2.HttpException
import shiverawe.github.com.receipt.data.bd.ReceiptDatabase
import shiverawe.github.com.receipt.data.network.MonthNetwork
import shiverawe.github.com.receipt.data.network.utils.UtilsNetwork
import shiverawe.github.com.receipt.data.network.entity.report.ReportRequest
import shiverawe.github.com.receipt.entity.receipt.base.Receipt
import shiverawe.github.com.receipt.entity.receipt.month.ReceiptMonth
import java.util.*
import kotlin.collections.ArrayList

class MonthRepository {

    private val network = MonthNetwork()
    private val db = ReceiptDatabase()

    fun getMonthReceipt(reportRequest: ReportRequest): Single<ArrayList<ReceiptMonth>> {
        return network.getMonthReceipts(reportRequest)
                .flatMap { networkReceipts ->
                    val dateFrom = reportRequest.meta.date_from.toLong() * 1000L
                    val dateTo = reportRequest.meta.date_to.toLong() * 1000L
                    db.updateMonthCache(dateFrom, dateTo, networkReceipts)
                }
                .map { receipts -> mapToMonthReceipt(receipts) }
                .onErrorResumeNext {
                    if (it is HttpException || !UtilsNetwork.isOnline()) {
                        val dateFrom = reportRequest.meta.date_from.toLong() * 1000L
                        val dateTo = reportRequest.meta.date_to.toLong() * 1000L
                        db.getReceipts(dateFrom, dateTo)
                                .map { receipts -> mapToMonthReceipt(receipts) }
                    } else {
                        Single.error(it)
                    }
                }
    }

    private fun mapToMonthReceipt(receipts: ArrayList<Receipt>): ArrayList<ReceiptMonth> {
        return ArrayList(receipts.map { ReceiptMonth(it.receiptId, it.shop, it.meta, 0)})
    }
}