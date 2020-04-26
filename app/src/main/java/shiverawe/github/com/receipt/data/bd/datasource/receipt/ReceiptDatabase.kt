package shiverawe.github.com.receipt.data.bd.datasource.receipt

import androidx.room.Transaction
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import shiverawe.github.com.receipt.data.bd.room.ReceiptRoom
import shiverawe.github.com.receipt.domain.entity.dto.base.Receipt

class ReceiptDatabase: IReceiptDatabase {

    private val db = ReceiptRoom.getDb()

    @Transaction
    override fun getReceiptById(receiptId: Long): Single<Receipt> {
        return Single.create<Receipt> {
            emitter ->
            val receipt = db.receiptDao().getReceiptById(receiptId)
            val products = db.productDao().getProductsForReceipts(arrayOf(receiptId))
            emitter.onSuccess(db.mapper.dbToReceipt(receipt, products))
        }.subscribeOn(Schedulers.io())
    }
}