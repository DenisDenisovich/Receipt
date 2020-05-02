package shiverawe.github.com.receipt.domain.repository

import io.reactivex.Observable
import io.reactivex.Single
import shiverawe.github.com.receipt.data.network.entity.create.CreateResponse
import shiverawe.github.com.receipt.domain.entity.dto.Meta
import shiverawe.github.com.receipt.domain.entity.dto.Receipt

interface IReceiptRepository {
    fun getReceipt(meta: Meta): Single<Receipt?>
    fun saveReceipt(meta: Meta): Single<Long>
    fun getReceiptById(receiptId: Long): Observable<Receipt>
}