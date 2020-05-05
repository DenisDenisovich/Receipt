package shiverawe.github.com.receipt.data.bd.datasource.receipt

import androidx.room.Transaction
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import shiverawe.github.com.receipt.data.bd.mapper.toReceipt
import shiverawe.github.com.receipt.data.bd.mapper.toReceiptHeader
import shiverawe.github.com.receipt.data.bd.room.ReceiptRoom
import shiverawe.github.com.receipt.domain.entity.base.Product
import shiverawe.github.com.receipt.domain.entity.base.Receipt
import shiverawe.github.com.receipt.domain.entity.base.ReceiptHeader

class ReceiptDatabase : IReceiptDatabase {

    private val db = ReceiptRoom.getDb()

    @Transaction
    override fun saveProductsToCache(remoteReceiptId: Long, networkProducts: List<Product>): Single<Receipt> =
        Single.create<Receipt> { emitter ->
            val receiptDb = db.receiptDao().getReceiptByRemoteId(remoteReceiptId)
            val receiptId = receiptDb.id ?: 0
            val localProductsDb = db.productDao().getProductsForReceiptIds(arrayOf(receiptId))
            if (localProductsDb.isEmpty()) {
                // DB doesn't contain products for this period. Save new data
                db.saveProducts(receiptId, networkProducts)
            }
            val receiptHeader = receiptDb.toReceiptHeader()
            emitter.onSuccess(Receipt(receiptHeader, networkProducts))
        }.subscribeOn(Schedulers.io())

    @Transaction
    override fun getReceiptById(remoteReceiptId: Long): Single<Receipt> =
        Single.create<Receipt> { emitter ->
            val receiptDb = db.receiptDao().getReceiptByRemoteId(remoteReceiptId)
            val productsDb = db.productDao().getProductsForReceiptIds(arrayOf(receiptDb.id))
            emitter.onSuccess(receiptDb.toReceipt(productsDb))
        }.subscribeOn(Schedulers.io())

    override fun getReceiptHeaderById(remoteReceiptId: Long): Single<ReceiptHeader> =
        Single.create<ReceiptHeader> { emitter ->
            val receiptDb = db.receiptDao().getReceiptByRemoteId(remoteReceiptId)
            emitter.onSuccess(receiptDb.toReceiptHeader())
        }.subscribeOn(Schedulers.io())
}