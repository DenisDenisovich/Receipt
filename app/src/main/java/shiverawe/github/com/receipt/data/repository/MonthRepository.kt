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

    override suspend fun getMonthReceipt(dateFrom: Long, dateTo: Long): List<ReceiptHeader> =
        network.getMonthReceipts(dateFrom.toStringWithSeconds(), dateTo.toStringWithSeconds())

    override suspend fun updateMonthCache(dateFrom: Long, dateTo: Long, networkReceipts: List<ReceiptHeader>) {
        db.updateMonthCache(dateFrom, dateTo, networkReceipts)
    }

    override suspend fun getMonthReceiptFromDb(dateFrom: Long, dateTo: Long): List<ReceiptHeader> =
        db.getReceiptHeaders(dateFrom, dateTo)
}