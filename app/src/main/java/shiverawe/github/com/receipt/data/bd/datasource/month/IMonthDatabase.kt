package shiverawe.github.com.receipt.data.bd.datasource.month

import io.reactivex.Single
import shiverawe.github.com.receipt.domain.entity.dto.ReceiptHeader

interface IMonthDatabase {

    fun updateMonthCache(dateFrom: Long, dateTo: Long, networkReceipts: List<ReceiptHeader>): Single<List<ReceiptHeader>>

    fun getReceiptHeaders(dataFrom: Long, dataTo: Long): Single<List<ReceiptHeader>>
}