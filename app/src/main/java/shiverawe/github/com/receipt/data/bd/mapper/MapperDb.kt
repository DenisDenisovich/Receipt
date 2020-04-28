package shiverawe.github.com.receipt.data.bd.mapper

import shiverawe.github.com.receipt.data.bd.room.product.ProductEntity
import shiverawe.github.com.receipt.data.bd.room.receipt.ReceiptEntity
import shiverawe.github.com.receipt.domain.entity.dto.*

class MapperDb : IMapperDb {

    override fun dbToReceipt(receipt: ReceiptEntity, products: List<ProductEntity>): Receipt {
        val shop = Shop(receipt.date, receipt.place, receipt.sum.toString())
        val meta = Meta(receipt.date, receipt.fn, receipt.fd, receipt.fp, receipt.sum)
        val items = products.map { Product(it.text, it.price, it.amount.toDouble()) }.toCollection(ArrayList())
        return Receipt(receipt.remoteId, shop, meta, items)
    }

    override fun dbToReceiptHeader(receipt: ReceiptEntity): ReceiptHeader {
        val shop = Shop(receipt.date, receipt.place, receipt.sum.toString())
        val meta = Meta(receipt.date, receipt.fn, receipt.fd, receipt.fp, receipt.sum)
        return ReceiptHeader(receipt.remoteId, shop, meta)
    }

    override fun receiptToDb(receipt: Receipt): ReceiptEntity = ReceiptEntity(
        receipt.meta.t,
        receipt.shop.place,
        receipt.meta.s,
        receipt.meta.fn,
        receipt.meta.fd,
        receipt.meta.fp,
        receipt.receiptId
    )

    override fun receiptHeaderToDb(receipt: ReceiptHeader): ReceiptEntity = ReceiptEntity(
        receipt.meta.t,
        receipt.shop.place,
        receipt.meta.s,
        receipt.meta.fn,
        receipt.meta.fd,
        receipt.meta.fp,
        receipt.receiptId
    )

    override fun productToDb(product: Product, receiptId: Long): ProductEntity =
        ProductEntity(product.amount.toFloat(), product.text, product.price, receiptId)
}