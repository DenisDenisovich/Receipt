package shiverawe.github.com.receipt.data.bd.datasource.receipt

import io.reactivex.Single
import shiverawe.github.com.receipt.domain.entity.base.Product
import shiverawe.github.com.receipt.domain.entity.base.Receipt
import shiverawe.github.com.receipt.domain.entity.base.ReceiptHeader

interface IReceiptDatabase {

    fun saveProductsToCache(remoteReceiptId: Long, networkProducts: List<Product>): Single<Receipt>

    fun getReceiptById(remoteReceiptId: Long): Single<Receipt>

    fun getReceiptHeaderById(remoteReceiptId: Long): Single<ReceiptHeader>
}