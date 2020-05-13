package shiverawe.github.com.receipt.domain.repository

import shiverawe.github.com.receipt.domain.entity.base.ReceiptHeader

interface IMonthRepository {

    suspend fun getMonthReceipt(dateFrom: Long, dateTo: Long): List<ReceiptHeader>

    suspend fun updateMonthCache(dateFrom: Long, dateTo: Long, networkReceipts: List<ReceiptHeader>)

    suspend fun getMonthReceiptFromDb(dateFrom: Long, dateTo: Long): List<ReceiptHeader>
}