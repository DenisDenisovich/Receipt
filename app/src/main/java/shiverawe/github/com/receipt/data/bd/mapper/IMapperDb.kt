package shiverawe.github.com.receipt.data.bd.mapper

import shiverawe.github.com.receipt.data.bd.room.product.ProductEntity
import shiverawe.github.com.receipt.data.bd.room.receipt.ReceiptEntity
import shiverawe.github.com.receipt.domain.entity.dto.base.Product
import shiverawe.github.com.receipt.domain.entity.dto.base.Receipt

interface IMapperDb {
    fun dbToReceipt(receipt: ReceiptEntity, products: List<ProductEntity>): Receipt
    fun receiptToDb(receipt: Receipt): ReceiptEntity
    fun productToDb(product: Product, savedId: Long): ProductEntity
}