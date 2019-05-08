package shiverawe.github.com.receipt.data.bd.mapper

import shiverawe.github.com.receipt.data.bd.product.ProductEntity
import shiverawe.github.com.receipt.data.bd.receipt.ReceiptEntity
import shiverawe.github.com.receipt.domain.entity.receipt.base.Product
import shiverawe.github.com.receipt.domain.entity.receipt.base.Receipt

interface IMapperDb {
    fun dbToReceipt(receipt: ReceiptEntity, products: List<ProductEntity>): Receipt
    fun receiptToDb(receipt: Receipt): ReceiptEntity
    fun productToDb(product: Product, savedId: Long): ProductEntity
}