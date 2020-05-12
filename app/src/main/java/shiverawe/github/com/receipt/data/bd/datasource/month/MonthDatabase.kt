package shiverawe.github.com.receipt.data.bd.datasource.month

import shiverawe.github.com.receipt.data.bd.mapper.toReceiptHeader
import shiverawe.github.com.receipt.data.bd.room.ReceiptRoom
import shiverawe.github.com.receipt.data.bd.utils.ICacheDiffUtility
import shiverawe.github.com.receipt.domain.entity.base.ReceiptHeader

class MonthDatabase(private val diffUtility: ICacheDiffUtility) : IMonthDatabase {

    private val db = ReceiptRoom.getDb()

    override suspend fun updateMonthCache(
        dateFrom: Long,
        dateTo: Long,
        networkReceipts: List<ReceiptHeader>
    ): List<ReceiptHeader> {
        val localReceipts = db.getReceiptHeaders(dateFrom, dateTo)

        if (localReceipts.isEmpty()) {
            // DB doesn't contain receipts for this period. Save new data
            db.saveReceiptHeaders(networkReceipts)
        } else {
            // DB already contain data for this period. Update DB
            val (deletedIds, newNetwork) = diffUtility.findDiffReceiptsHeader(localReceipts, networkReceipts)
            db.receiptDao().removeReceiptHeadersByIds(deletedIds.toTypedArray())
            db.saveReceiptHeaders(newNetwork)
        }

        return networkReceipts.sortedByDescending { it.meta.t }
    }


    override suspend fun getReceiptHeaders(dataFrom: Long, dataTo: Long): List<ReceiptHeader> =
        db.receiptDao()
            .getReceiptHeaders(dataFrom, dataTo)
            .asSequence()
            .sortedByDescending { it.date }
            .map { it.toReceiptHeader() }
            .toList()
}