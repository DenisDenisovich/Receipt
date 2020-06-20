package shiverawe.github.com.receipt.data.network.datasource.month

import shiverawe.github.com.receipt.data.network.api.Api
import shiverawe.github.com.receipt.data.network.entity.receipt.ReceiptRequest
import shiverawe.github.com.receipt.data.network.mapper.toReceiptHeader
import shiverawe.github.com.receipt.domain.entity.base.ReceiptHeader

class MonthNetwork(private val api: Api) : IMonthNetwork {

    override suspend fun getMonthReceipts(dateFrom: String, dateTo: String): List<ReceiptHeader> =
        api.getReceipts(ReceiptRequest(dateFrom = dateFrom, dateTo = dateTo)).toReceiptHeader()
}