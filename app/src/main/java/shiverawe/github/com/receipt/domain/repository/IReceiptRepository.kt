package shiverawe.github.com.receipt.domain.repository

import shiverawe.github.com.receipt.domain.entity.base.Meta
import shiverawe.github.com.receipt.domain.entity.base.Product
import shiverawe.github.com.receipt.domain.entity.base.Receipt
import shiverawe.github.com.receipt.domain.entity.base.ReceiptHeader

interface IReceiptRepository {

    suspend fun createReceipt(meta: Meta): ReceiptHeader

    suspend fun getReceipt(receiptId: Long): Receipt?

    suspend fun getReceiptHeader(receiptId: Long): ReceiptHeader?

    suspend fun getProducts(id: Long): List<Product>

    suspend fun saveProducts(receiptId: Long, products: List<Product>)
}