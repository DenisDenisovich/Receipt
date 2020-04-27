package shiverawe.github.com.receipt.data.bd.datasource.month

import io.reactivex.Single
import shiverawe.github.com.receipt.data.bd.room.ReceiptRoom
import shiverawe.github.com.receipt.data.bd.utils.ICacheDiffUtility
import shiverawe.github.com.receipt.domain.entity.dto.Receipt
import shiverawe.github.com.receipt.domain.entity.dto.ReceiptHeader

class MonthDatabase(private val cacheDiffUtility: ICacheDiffUtility) : IMonthDatabase {

    private val db = ReceiptRoom.getDb()

    override fun updateMonthCache(
        dateFrom: Long,
        dateTo: Long,
        networkReceipts: ArrayList<ReceiptHeader>
    ): Single<ArrayList<ReceiptHeader>> =
        Single.create { emitter ->
            val localReceipts = db.getReceiptHeaders(dateFrom, dateTo)
            if (localReceipts.size == 0) {
                // DB doesn't contain receipts for this period. Save new data
                db.saveReceiptHeaders(networkReceipts)
            } else {
                // DB already contain data for this period. Update DB
                val (deletedIds, newNetwork) =
                    cacheDiffUtility.findDiffReceiptsHeader(localReceipts, networkReceipts)
                db.receiptDao().removeReceiptHeadersByIds(deletedIds.toTypedArray())
                db.saveReceiptHeaders(newNetwork)
            }
            emitter.onSuccess(networkReceipts)
        }

    override fun getReceiptHeaders(dataFrom: Long, dataTo: Long): Single<ArrayList<ReceiptHeader>> =
        Single.create { emitter ->
            val receipts = db.receiptDao()
                .getReceiptHeaders(dataFrom, dataTo)
                .asSequence()
                .sortedByDescending { it.date }
                .map { db.mapper.dbToReceiptHeader(it) }
                .toCollection(ArrayList())
            emitter.onSuccess(receipts)
        }
}