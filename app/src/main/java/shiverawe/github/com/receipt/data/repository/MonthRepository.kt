package shiverawe.github.com.receipt.data.repository

import io.reactivex.Single
import retrofit2.HttpException
import shiverawe.github.com.receipt.data.bd.ReceiptDatabase
import shiverawe.github.com.receipt.data.network.MonthNetwork
import shiverawe.github.com.receipt.data.network.UtilsNetwork
import shiverawe.github.com.receipt.data.network.entity.report.ReportRequest
import shiverawe.github.com.receipt.entity.Receipt
import java.util.*
import kotlin.collections.ArrayList

class MonthRepository {

    private val network = MonthNetwork()
    private val db = ReceiptDatabase.getDb()

    fun getMonthReceipt(reportRequest: ReportRequest): Single<ArrayList<Receipt?>> {
        return network.getMonthReceipts(reportRequest)
                .map { it -> db.updateMonthCache(reportRequest, it) }
                .map { it -> network.addWeekSeparators(it) }
                .onErrorResumeNext {
                    if (it is HttpException || !UtilsNetwork.isOnline()) {
                        val cachedData = db.getReceipts(reportRequest.meta.date_from.toLong() * 1000L, reportRequest.meta.date_to.toLong() * 1000L)
                        Single.just(cachedData)
                                .map { cachedReceipts -> network.addWeekSeparators(cachedReceipts) }
                    } else {
                        Single.error(it)
                    }
                }
    }
}