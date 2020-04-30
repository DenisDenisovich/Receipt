package shiverawe.github.com.receipt.domain.repository

import io.reactivex.Single
import shiverawe.github.com.receipt.domain.entity.dto.ReceiptHeader

interface IMonthRepository {
    fun getMonthReceipt(dateFrom: Long, dateTo: Long): Single<List<ReceiptHeader>>
}