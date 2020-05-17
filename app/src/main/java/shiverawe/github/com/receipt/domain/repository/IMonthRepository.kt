package shiverawe.github.com.receipt.domain.repository

import shiverawe.github.com.receipt.domain.entity.base.ReceiptHeader

interface IMonthRepository {

    suspend fun getNetworkReceipt(dateFrom: Long, dateTo: Long): List<ReceiptHeader>

    suspend fun updateCache(dateFrom: Long, dateTo: Long, networkReceipts: List<ReceiptHeader>)

    suspend fun getReceiptFromDb(dateFrom: Long, dateTo: Long): List<ReceiptHeader>
}