package shiverawe.github.com.receipt.data.bd.mapper

import shiverawe.github.com.receipt.data.bd.room.product.ProductEntity
import shiverawe.github.com.receipt.data.bd.room.receipt.ReceiptEntity
import shiverawe.github.com.receipt.domain.entity.base.*

fun ReceiptEntity.toReceipt(products: List<ProductEntity>): Receipt {
    val shop = Shop(date, place, address, sum.toString())
    val meta = Meta(date, fn, fd, fp, sum)
    val items = products.map { Product(it.text, it.price, it.amount.toDouble()) }
    return Receipt(ReceiptHeader(id, ReceiptStatus.LOADED, shop, meta), items)
}

fun ReceiptEntity.toReceiptHeader(): ReceiptHeader {
    val shop = Shop(date, place, address, sum.toString())
    val meta = Meta(date, fn, fd, fp, sum)
    return ReceiptHeader(id, ReceiptStatus.LOADED, shop, meta)
}

fun Receipt.toReceiptEntity(): ReceiptEntity = ReceiptEntity(
    header.receiptId,
    header.meta.t,
    header.shop.title,
    header.meta.s,
    header.meta.fn,
    header.meta.fd,
    header.meta.fp,
    header.shop.address
)

fun ReceiptHeader.toReceiptEntity(): ReceiptEntity = ReceiptEntity(
    receiptId,
    meta.t,
    shop.title,
    meta.s,
    meta.fn,
    meta.fd,
    meta.fp,
    shop.address
)

fun Product.toProductEntity(receiptId: Long): ProductEntity =
    ProductEntity(amount.toFloat(), text, price, receiptId)
