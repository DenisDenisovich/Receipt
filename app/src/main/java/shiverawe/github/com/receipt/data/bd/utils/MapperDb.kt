package shiverawe.github.com.receipt.data.bd.utils

import shiverawe.github.com.receipt.data.bd.product.ProductEntity
import shiverawe.github.com.receipt.data.bd.receipt.ReceiptEntity
import shiverawe.github.com.receipt.entity.receipt.base.Meta
import shiverawe.github.com.receipt.entity.receipt.base.Product
import shiverawe.github.com.receipt.entity.receipt.base.Receipt
import shiverawe.github.com.receipt.entity.receipt.base.Shop

class MapperDb {

    fun dbToReceipt(receipt: ReceiptEntity, products: List<ProductEntity>): Receipt {
        val shop = Shop(receipt.date, receipt.place, receipt.sum.toString())
        val meta = Meta(receipt.date.toString(), receipt.fn, receipt.fd, receipt.fp, receipt.sum)
        val items = ArrayList<Product>()
        products.forEach {
            items.add(Product(it.text, it.price, it.amount.toDouble()))
        }
        return Receipt(receipt.id!!, shop, meta, items)
    }

    fun receiptToDb(receipt: Receipt): ReceiptEntity {
        return ReceiptEntity(receipt.meta.t.toLong(), receipt.shop.place, receipt.meta.s, receipt.meta.fn, receipt.meta.fd, receipt.meta.fp)
    }

    fun productToDb(product: Product, savedId: Long): ProductEntity {
        return ProductEntity(product.amount.toFloat(), product.text, product.price, savedId)
    }

}