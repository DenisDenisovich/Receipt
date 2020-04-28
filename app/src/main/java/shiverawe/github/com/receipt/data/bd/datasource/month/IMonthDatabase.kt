package shiverawe.github.com.receipt.data.bd.datasource.month

import io.reactivex.Single
import shiverawe.github.com.receipt.domain.entity.dto.ReceiptHeader

interface IMonthDatabase {

    fun updateMonthCache(dateFrom: Long, dateTo: Long, networkReceipts: ArrayList<ReceiptHeader>): Single<ArrayList<ReceiptHeader>>

    fun getReceiptHeaders(dataFrom: Long, dataTo: Long): Single<ArrayList<ReceiptHeader>>
}