package shiverawe.github.com.receipt.domain.repository

import io.reactivex.Single
import shiverawe.github.com.receipt.data.network.entity.create.CreateResponce
import shiverawe.github.com.receipt.domain.entity.receipt.base.Receipt

interface IReceiptRepository {
    fun getReceipt(options: Map<String, String>): Single<Receipt?>
    fun saveReceipt(): Single<CreateResponce>
    fun getReceiptById(receiptId: Long): Single<Receipt>
}