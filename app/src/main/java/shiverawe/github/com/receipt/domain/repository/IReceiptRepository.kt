package shiverawe.github.com.receipt.domain.repository

import io.reactivex.Observable
import io.reactivex.Single
import shiverawe.github.com.receipt.data.network.entity.create.CreateResponce
import shiverawe.github.com.receipt.domain.entity.dto.Meta
import shiverawe.github.com.receipt.domain.entity.dto.Receipt

interface IReceiptRepository {
    fun getReceipt(meta: Meta): Single<Receipt?>
    fun saveReceipt(): Single<CreateResponce>
    fun getReceiptById(receiptId: Long): Observable<Receipt>
}