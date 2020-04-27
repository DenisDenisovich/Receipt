package shiverawe.github.com.receipt.data.network.datasource.month

import io.reactivex.Single
import shiverawe.github.com.receipt.domain.entity.dto.ReceiptHeader

interface IMonthNetwork {
    fun getMonthReceipts(dateFrom: String, dateTo: String): Single<ArrayList<ReceiptHeader>>
}