package shiverawe.github.com.receipt.data.bd.datasource.month

import shiverawe.github.com.receipt.domain.entity.base.ReceiptHeader

interface IMonthDatabase {

    suspend fun updateMonthCache(dateFrom: Long, dateTo: Long, networkReceipts: List<ReceiptHeader>): List<ReceiptHeader>

    suspend fun getReceiptHeaders(dataFrom: Long, dataTo: Long): List<ReceiptHeader>
}