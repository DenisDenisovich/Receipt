package shiverawe.github.com.receipt.data.bd.datasource.receipt

import androidx.room.Transaction
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import shiverawe.github.com.receipt.data.bd.room.ReceiptRoom
import shiverawe.github.com.receipt.domain.entity.dto.Product
import shiverawe.github.com.receipt.domain.entity.dto.Receipt
import shiverawe.github.com.receipt.domain.entity.dto.ReceiptHeader

class ReceiptDatabase : IReceiptDatabase {

    private val db = ReceiptRoom.getDb()

    @Transaction
    override fun updateProductsCache(remoteReceiptId: Long, networkProducts: ArrayList<Product>): Single<Receipt> =
        Single.create<Receipt> { emitter ->
            val receipt = db.receiptDao().getReceiptByRemoteId(remoteReceiptId)
            val receiptId = receipt.id ?: 0
            val localProducts = db.productDao().getProductsForReceiptIds(arrayOf(receiptId))
            if (localProducts.isEmpty()) {
                // DB doesn't contain products for this period. Save new data
                db.saveProducts(receiptId, networkProducts)
            }
            val receiptHeader = db.mapper.dbToReceiptHeader(receipt)
            emitter.onSuccess(Receipt(receiptHeader.receiptId, receiptHeader.shop, receiptHeader.meta, networkProducts))
        }.subscribeOn(Schedulers.io())

    @Transaction
    override fun getReceiptById(remoteReceiptId: Long): Single<Receipt> =
        Single.create<Receipt> { emitter ->
            val receipt = db.receiptDao().getReceiptByRemoteId(remoteReceiptId)
            val products = db.productDao().getProductsForReceiptIds(arrayOf(receipt.id))
            emitter.onSuccess(db.mapper.dbToReceipt(receipt, products))
        }.subscribeOn(Schedulers.io())

    override fun getReceiptHeaderById(remoteReceiptId: Long): Single<ReceiptHeader> =
        Single.create<ReceiptHeader> { emitter ->
            val receipt = db.receiptDao().getReceiptByRemoteId(remoteReceiptId)
            emitter.onSuccess(db.mapper.dbToReceiptHeader(receipt))
        }.subscribeOn(Schedulers.io())
}