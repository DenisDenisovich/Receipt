package shiverawe.github.com.receipt.data.repository

import shiverawe.github.com.receipt.data.bd.datasource.month.IMonthDatabase
import shiverawe.github.com.receipt.data.network.datasource.month.IMonthNetwork
import shiverawe.github.com.receipt.domain.repository.IMonthRepository
import shiverawe.github.com.receipt.domain.entity.base.ReceiptHeader
import shiverawe.github.com.receipt.utils.toStringWithSeconds

class MonthRepository(
    private val network: IMonthNetwork,
    private val db: IMonthDatabase
) : IMonthRepository {

    override suspend fun getMonthReceipt(dateFrom: Long, dateTo: Long): List<ReceiptHeader> {
        val networkReceipts = network.getMonthReceipts(
            dateFrom.toStringWithSeconds(),
            dateTo.toStringWithSeconds()
        )

        return db.updateMonthCache(dateFrom, dateTo, networkReceipts)
    }

    override suspend fun getMonthReceiptFromDb(dateFrom: Long, dateTo: Long): List<ReceiptHeader> =
        db.getReceiptHeaders(dateFrom, dateTo)
/*
        network.getMonthReceipts(formatter.format(dateFrom), formatter.format(dateTo))
                .flatMap { networkReceipts ->
                    db.updateMonthCache(dateFrom, dateTo, networkReceipts)
                }
                .onErrorResumeNext {
                    if (it is HttpException || !isOnline()) {
                        db.getReceiptHeaders(dateFrom, dateTo)
                    } else {
                        Single.error(it)
                    }
                }
*/
}