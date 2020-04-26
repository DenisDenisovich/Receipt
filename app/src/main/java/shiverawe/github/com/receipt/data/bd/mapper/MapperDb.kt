package shiverawe.github.com.receipt.data.bd.mapper

import shiverawe.github.com.receipt.data.bd.room.product.ProductEntity
import shiverawe.github.com.receipt.data.bd.room.receipt.ReceiptEntity
import shiverawe.github.com.receipt.domain.entity.dto.Meta
import shiverawe.github.com.receipt.domain.entity.dto.Product
import shiverawe.github.com.receipt.domain.entity.dto.Receipt
import shiverawe.github.com.receipt.domain.entity.dto.Shop

class MapperDb : IMapperDb {

    override fun dbToReceipt(receipt: ReceiptEntity, products: List<ProductEntity>): Receipt {
        val shop = Shop(receipt.date, receipt.place, receipt.sum.toString())
        val meta = Meta(receipt.date, receipt.fn, receipt.fd, receipt.fp, receipt.sum)
        val items = products.map { Product(it.text, it.price, it.amount.toDouble()) }.toCollection(ArrayList())
        return Receipt(receipt.id!!, shop, meta, items)
    }

    override fun receiptToDb(receipt: Receipt): ReceiptEntity = ReceiptEntity(
        receipt.meta.t,
        receipt.shop.place,
        receipt.meta.s,
        receipt.meta.fn,
        receipt.meta.fd,
        receipt.meta.fp
    )

    override fun productToDb(product: Product, savedId: Long): ProductEntity =
        ProductEntity(product.amount.toFloat(), product.text, product.price, savedId)
}