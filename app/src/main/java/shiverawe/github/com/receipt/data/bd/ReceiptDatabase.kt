package shiverawe.github.com.receipt.data.bd

import android.arch.persistence.room.Transaction
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import shiverawe.github.com.receipt.data.bd.utils.CacheDiffUtility
import shiverawe.github.com.receipt.entity.Receipt

class ReceiptDatabase {
    private val db = ReceiptRoom.getDb()
    private val cacheDiffUtility = CacheDiffUtility()
    fun updateMonthCache(dateFrom: Long, dateTo: Long, networkReceipts: ArrayList<Receipt>): Single<ArrayList<Receipt>> {
        return Single.create{
            emitter ->
            val localReceipts = db.getReceiptsWithProducts(dateFrom, dateTo)
            val localIds: List<Long>
            if (localReceipts.size == 0) {
                localIds = db.saveReceipts(networkReceipts)
            } else {
                val (deletedIds, newNetwork) = cacheDiffUtility.findDiffReceipts(localReceipts, networkReceipts)
                db.receiptDao().removeMonthReceiptsByIds(deletedIds.toTypedArray())
                db.saveReceipts(newNetwork)
                localIds = db.receiptDao().getMonthReceiptsId(dateFrom, dateTo)
            }
            val receipts = ArrayList<Receipt>()
            for (i in 0 until networkReceipts.size) {
                receipts.add(Receipt(localIds[i], networkReceipts[i].shop, networkReceipts[i].meta, ArrayList()))
            }
            emitter.onSuccess(receipts)
        }
    }

    @Transaction
    fun getReceiptById(receiptId: Long): Single<Receipt> {
        return Single.create<Receipt> {
            emitter ->
            val receipt = db.receiptDao().getReceiptById(receiptId)
            val products = db.productDao().getProductsForReceipts(arrayOf(receiptId))
            emitter.onSuccess(db.mapper.dbToReceipt(receipt, products))
        }.subscribeOn(Schedulers.io())
    }

    fun getReceipts(dataFrom: Long, dataTo: Long): Single<ArrayList<Receipt>> {
        return Single.create {
            emitter ->
            val receipts = ArrayList<Receipt>()
            var receiptsDb = db.receiptDao().getMonthReceipts(dataFrom, dataTo)
            receiptsDb = receiptsDb.sortedByDescending { it.date }
            receiptsDb.forEach {
                receiptDb ->
                receipts.add(db.mapper.dbToReceipt(receiptDb, ArrayList()))
            }
            emitter.onSuccess(receipts)
        }
    }
}