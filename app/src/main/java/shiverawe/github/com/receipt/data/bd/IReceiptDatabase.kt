package shiverawe.github.com.receipt.data.bd

import io.reactivex.Single
import shiverawe.github.com.receipt.domain.entity.receipt.base.Receipt

interface IReceiptDatabase {
    fun updateMonthCache(dateFrom: Long, dateTo: Long, networkReceipts: ArrayList<Receipt>): Single<ArrayList<Receipt>>
    fun getReceiptById(receiptId: Long): Single<Receipt>
    fun getReceipts(dataFrom: Long, dataTo: Long): Single<ArrayList<Receipt>>

}