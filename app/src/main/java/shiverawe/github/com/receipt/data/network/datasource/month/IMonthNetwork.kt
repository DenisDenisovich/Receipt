package shiverawe.github.com.receipt.data.network.datasource.month

import shiverawe.github.com.receipt.domain.entity.base.ReceiptHeader

interface IMonthNetwork {
    suspend fun getMonthReceipts(dateFrom: String, dateTo: String): List<ReceiptHeader>
}