package shiverawe.github.com.receipt.data.bd.datasource.month

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import shiverawe.github.com.receipt.data.bd.mapper.toReceiptHeader
import shiverawe.github.com.receipt.data.bd.room.ReceiptRoom
import shiverawe.github.com.receipt.domain.entity.base.ReceiptHeader

class MonthDatabase : IMonthDatabase {

    private val db = ReceiptRoom.getDb()

    override suspend fun updateMonthCache(dateFrom: Long, dateTo: Long, networkReceipts: List<ReceiptHeader>) {
        withContext(Dispatchers.IO) {
            val localReceipts = db.getReceiptHeaders(dateFrom, dateTo)

            if (localReceipts.isEmpty()) {
                // DB doesn't contain receipts for this period. Save new data
                db.saveReceiptHeaders(networkReceipts)
            } else {
                // DB already contain data for this period. Update DB
                db.receiptDao().removeMonthReceipts(dateFrom, dateTo)
                db.saveReceiptHeaders(networkReceipts)
            }
        }
    }

    override suspend fun getReceiptHeaders(dataFrom: Long, dataTo: Long): List<ReceiptHeader> =
        withContext(Dispatchers.IO) {
            db.receiptDao()
                .getReceiptHeaders(dataFrom, dataTo)
                .asSequence()
                .sortedByDescending { it.date }
                .map { it.toReceiptHeader() }
                .toList()
        }
}