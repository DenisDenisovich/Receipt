package shiverawe.github.com.receipt.domain.repository

import io.reactivex.Observable
import io.reactivex.Single
import shiverawe.github.com.receipt.domain.entity.base.Meta
import shiverawe.github.com.receipt.domain.entity.base.Receipt

interface IReceiptRepository {
    fun getReceipt(meta: Meta): Single<Receipt?>
    suspend fun saveReceipt(meta: Meta): Long
    fun getReceiptById(receiptId: Long): Observable<Receipt>
}