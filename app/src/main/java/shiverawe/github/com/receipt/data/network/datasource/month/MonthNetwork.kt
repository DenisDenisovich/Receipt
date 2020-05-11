package shiverawe.github.com.receipt.data.network.datasource.month

import io.reactivex.Single
import shiverawe.github.com.receipt.data.network.api.Api
import shiverawe.github.com.receipt.data.network.entity.receipt.ReceiptRequest
import shiverawe.github.com.receipt.data.network.mapper.toReceiptHeader
import shiverawe.github.com.receipt.domain.entity.base.ReceiptHeader
import shiverawe.github.com.receipt.domain.entity.base.ReceiptStatus

class MonthNetwork(private val api: Api) : IMonthNetwork {

    override fun getMonthReceipts(dateFrom: String, dateTo: String): Single<List<ReceiptHeader>> =
        api.getReceipts(ReceiptRequest(dateFrom = dateFrom, dateTo = dateTo))
            .map { response ->
                response.filter {
                    it.status == ReceiptStatus.LOADED.name &&
                        it.sum != null &&
                        it.date != null
                }.toReceiptHeader()
            }
}