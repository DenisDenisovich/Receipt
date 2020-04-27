package shiverawe.github.com.receipt.data.network.datasource.month

import io.reactivex.Single
import shiverawe.github.com.receipt.data.network.api.Api
import shiverawe.github.com.receipt.data.network.entity.receipt.ReceiptRequest
import shiverawe.github.com.receipt.data.network.mapper.IMapperNetwork
import shiverawe.github.com.receipt.domain.entity.dto.ReceiptHeader
import kotlin.collections.ArrayList

class MonthNetwork(
    private val mapper: IMapperNetwork,
    private val api: Api) : IMonthNetwork {
    override fun getMonthReceipts(dateFrom: String, dateTo: String): Single<ArrayList<ReceiptHeader>> =
        api.getReceiptForMonth(ReceiptRequest(dateFrom, dateTo))
            .map { response ->
                val filterResponse = response.filter {
                    it.status != "FAILED" &&
                        it.place != null &&
                        it.sum != null &&
                        it.date != null
                }
                mapper.toReceiptHeader(filterResponse)
            }
}