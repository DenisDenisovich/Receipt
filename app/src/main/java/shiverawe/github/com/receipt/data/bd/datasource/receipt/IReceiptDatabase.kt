package shiverawe.github.com.receipt.data.bd.datasource.receipt

import shiverawe.github.com.receipt.domain.entity.base.Product
import shiverawe.github.com.receipt.domain.entity.base.Receipt
import shiverawe.github.com.receipt.domain.entity.base.ReceiptHeader

interface IReceiptDatabase {

    suspend fun saveProductsToCache(remoteReceiptId: Long, products: List<Product>)

    suspend fun getReceiptById(remoteReceiptId: Long): Receipt?

    suspend fun getReceiptHeaderById(remoteReceiptId: Long): ReceiptHeader?
}