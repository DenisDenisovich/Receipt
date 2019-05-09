package shiverawe.github.com.receipt.data.bd.datasource.month

import io.reactivex.Single
import shiverawe.github.com.receipt.data.bd.room.ReceiptRoom
import shiverawe.github.com.receipt.data.bd.utils.ICacheDiffUtility
import shiverawe.github.com.receipt.domain.entity.dto.base.Receipt

class MonthDatabase(private val cacheDiffUtility: ICacheDiffUtility): IMonthDatabase {
    private val db = ReceiptRoom.getDb()
    override fun updateMonthCache(dateFrom: Long, dateTo: Long, networkReceipts: ArrayList<Receipt>): Single<ArrayList<Receipt>> {
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
    override fun getReceipts(dataFrom: Long, dataTo: Long): Single<ArrayList<Receipt>> {
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