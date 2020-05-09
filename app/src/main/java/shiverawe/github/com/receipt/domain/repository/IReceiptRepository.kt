package shiverawe.github.com.receipt.domain.repository

import io.reactivex.Observable
import io.reactivex.Single
import shiverawe.github.com.receipt.domain.entity.base.Meta
import shiverawe.github.com.receipt.domain.entity.base.Product
import shiverawe.github.com.receipt.domain.entity.base.Receipt
import shiverawe.github.com.receipt.domain.entity.base.ReceiptHeader

interface IReceiptRepository {
    fun getReceipt(meta: Meta): Single<Receipt?>

    suspend fun saveReceipt(meta: Meta): ReceiptHeader

    suspend fun getProducts(id: Long): List<Product>

    fun getReceiptById(receiptId: Long): Observable<Receipt>
}