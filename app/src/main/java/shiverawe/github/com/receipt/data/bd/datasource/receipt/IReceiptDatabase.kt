package shiverawe.github.com.receipt.data.bd.datasource.receipt

import io.reactivex.Single
import shiverawe.github.com.receipt.domain.entity.dto.Product
import shiverawe.github.com.receipt.domain.entity.dto.Receipt
import shiverawe.github.com.receipt.domain.entity.dto.ReceiptHeader

interface IReceiptDatabase {

    fun saveProductsToCache(remoteReceiptId: Long, networkProducts: List<Product>): Single<Receipt>

    fun getReceiptById(remoteReceiptId: Long): Single<Receipt>

    fun getReceiptHeaderById(remoteReceiptId: Long): Single<ReceiptHeader>
}