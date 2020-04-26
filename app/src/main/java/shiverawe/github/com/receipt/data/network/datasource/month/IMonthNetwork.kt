package shiverawe.github.com.receipt.data.network.datasource.month

import io.reactivex.Single
import shiverawe.github.com.receipt.domain.entity.dto.Receipt

interface IMonthNetwork {
    fun getMonthReceipts(dateFrom: String, dateTo: String): Single<ArrayList<Receipt>>
}