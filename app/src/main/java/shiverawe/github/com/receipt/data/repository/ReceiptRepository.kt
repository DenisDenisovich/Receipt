package shiverawe.github.com.receipt.data.repository

import shiverawe.github.com.receipt.data.bd.datasource.receipt.IReceiptDatabase
import shiverawe.github.com.receipt.data.network.datasource.receipt.IReceiptNetwork
import shiverawe.github.com.receipt.domain.entity.base.Meta
import shiverawe.github.com.receipt.domain.entity.base.Product
import shiverawe.github.com.receipt.domain.repository.IReceiptRepository
import shiverawe.github.com.receipt.domain.entity.base.Receipt
import shiverawe.github.com.receipt.domain.entity.base.ReceiptHeader

class ReceiptRepository(
    private val db: IReceiptDatabase,
    private val network: IReceiptNetwork
) : IReceiptRepository {

    override suspend fun createReceipt(meta: Meta): ReceiptHeader = network.createReceipt(meta)

    override suspend fun getReceipt(receiptId: Long): Receipt? =
        db.getReceiptById(receiptId) ?: network.getReceipt(receiptId)

    override suspend fun getReceiptHeader(receiptId: Long): ReceiptHeader? =
        db.getReceiptHeaderById(receiptId)

    override suspend fun getProducts(id: Long): List<Product> = network.getProducts(id)

    override suspend fun saveProducts(receiptId: Long, products: List<Product>) {
        db.saveProductsToCache(receiptId, products)
    }
}