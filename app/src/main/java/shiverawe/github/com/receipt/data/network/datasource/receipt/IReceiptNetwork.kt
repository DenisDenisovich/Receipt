package shiverawe.github.com.receipt.data.network.datasource.receipt

import shiverawe.github.com.receipt.domain.entity.base.Meta
import shiverawe.github.com.receipt.domain.entity.base.Product
import shiverawe.github.com.receipt.domain.entity.base.Receipt
import shiverawe.github.com.receipt.domain.entity.base.ReceiptHeader

interface IReceiptNetwork {

    suspend fun getReceipt(id: Long): Receipt?

    suspend fun getProducts(id: Long): List<Product>

    suspend fun createReceipt(meta: Meta): ReceiptHeader?
}