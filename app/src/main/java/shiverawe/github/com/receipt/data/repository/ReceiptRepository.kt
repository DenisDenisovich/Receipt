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

    override suspend fun getReceipt(receiptId: Long): Receipt? {
        val dbReceipt = db.getReceiptById(receiptId)

        return when {
            // db doesn't have receipt with this id. Get it from rest api
            dbReceipt == null -> {
                network.getReceipt(receiptId)
            }

            // db doesn't have products for this receipt. Get it from rest api and save to db
            dbReceipt.items.isEmpty() -> {
                val products = network.getProducts(receiptId)
                saveProducts(receiptId, products)
                Receipt(dbReceipt.header, products)
            }

            // return db receipt
            else -> {
                dbReceipt
            }
        }
    }

    override suspend fun getReceiptHeader(receiptId: Long): ReceiptHeader? =
        db.getReceiptHeaderById(receiptId)

    override suspend fun getProducts(id: Long): List<Product> = network.getProducts(id)

    override suspend fun saveProducts(receiptId: Long, products: List<Product>) {
        db.saveProductsToCache(receiptId, products)
    }
}