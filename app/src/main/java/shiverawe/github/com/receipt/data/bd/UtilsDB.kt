package shiverawe.github.com.receipt.data.bd

import com.google.gson.Gson
import shiverawe.github.com.receipt.data.bd.product.ProductEntity
import shiverawe.github.com.receipt.data.bd.receipt.ReceiptEntity
import shiverawe.github.com.receipt.entity.Meta
import shiverawe.github.com.receipt.entity.Product
import shiverawe.github.com.receipt.entity.Receipt
import shiverawe.github.com.receipt.entity.Shop

object UtilsDB {

    fun findDiffReceipts(localReceipts: ArrayList<Receipt>, networkReceipts: ArrayList<Receipt>): Pair<List<Long>,ArrayList<Receipt>> {
        val localHash = HashMap<String, Long>()
        val newNetwork = ArrayList<Receipt>()
        localReceipts.forEach {
            receipt ->
            val key = getKey(receipt)
            localHash[key] = receipt.receiptId
        }
        networkReceipts.forEach {
            networkReceipt ->
            val value = localHash.remove(getKey(networkReceipt))
            if (value == null) newNetwork.add(networkReceipt)
        }
        val deletedIds = localHash.map { it.value }
        return Pair(deletedIds, newNetwork)
    }


    private fun getKey(receipt: Receipt): String {
        val products = ArrayList(receipt.items.sortedBy { it.text })
        val keyReceipt = Receipt(0, receipt.shop, receipt.meta, products)
        return Gson().toJson(keyReceipt)
    }

    fun mapFromDb(receipt: ReceiptEntity, products: List<ProductEntity>): Receipt {
        val shop = Shop(receipt.date, receipt.place, receipt.sum.toString() + " р")
        val meta = Meta(receipt.date.toString(), receipt.fn, receipt.fd, receipt.fp, receipt.sum)
        val items = ArrayList<Product>()
        products.forEach {
            items.add(Product(it.text, it.price, it.amount.toDouble()))
        }
        return Receipt(receipt.id!!, shop, meta, items)
    }

    fun mapReceiptToDb(receipt: Receipt): ReceiptEntity {
        return ReceiptEntity(receipt.meta.t.toLong(), receipt.shop.place, receipt.meta.s, receipt.meta.fn, receipt.meta.fd, receipt.meta.fp)
    }

    fun mapProductToDb(product: Product, savedId: Long): ProductEntity {
        return ProductEntity(product.amount.toFloat(), product.text, product.price, savedId)
    }

}