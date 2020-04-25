package shiverawe.github.com.receipt.data.repository

import io.reactivex.Single
import retrofit2.HttpException
import shiverawe.github.com.receipt.data.bd.datasource.month.IMonthDatabase
import shiverawe.github.com.receipt.data.network.datasource.month.IMonthNetwork
import shiverawe.github.com.receipt.data.network.entity.receipt.ReportRequest
import shiverawe.github.com.receipt.data.network.utils.IUtilsNetwork
import shiverawe.github.com.receipt.domain.repository.IMonthRepository
import shiverawe.github.com.receipt.domain.entity.dto.base.Receipt
import shiverawe.github.com.receipt.domain.entity.dto.month.ReceiptMonth
import kotlin.collections.ArrayList

class MonthRepository(
        private val network: IMonthNetwork,
        private val db: IMonthDatabase,
        private val utils: IUtilsNetwork
) : IMonthRepository {

    override fun getMonthReceipt(reportRequest: ReportRequest): Single<ArrayList<ReceiptMonth>> =
        network.getMonthReceipts(reportRequest)
                .flatMap { networkReceipts ->
                    val dateFrom = reportRequest.meta.date_from.toLong() * 1000L
                    val dateTo = reportRequest.meta.date_to.toLong() * 1000L
                    db.updateMonthCache(dateFrom, dateTo, networkReceipts)
                }
                .map { receipts -> mapToMonthReceipt(receipts) }
                .onErrorResumeNext {
                    if (it is HttpException || !utils.isOnline()) {
                        val dateFrom = reportRequest.meta.date_from.toLong() * 1000L
                        val dateTo = reportRequest.meta.date_to.toLong() * 1000L
                        db.getReceipts(dateFrom, dateTo)
                                .map { receipts -> mapToMonthReceipt(receipts) }
                    } else {
                        Single.error(it)
                    }
                }

    private fun mapToMonthReceipt(receipts: ArrayList<Receipt>): ArrayList<ReceiptMonth> =
        ArrayList(receipts.map { ReceiptMonth(it.receiptId, it.shop, it.meta) })
}