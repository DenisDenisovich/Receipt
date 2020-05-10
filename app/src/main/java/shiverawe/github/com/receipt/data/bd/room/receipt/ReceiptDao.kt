package shiverawe.github.com.receipt.data.bd.room.receipt

import androidx.room.*

@Dao
interface ReceiptDao {

    @Insert
    fun addReceipts(receipts: List<ReceiptEntity>): List<Long>

    @Update
    fun updateReceipt(receipt: ReceiptEntity): Int

    @Delete
    fun deleteReceipt(receipt: ReceiptEntity): Int

    @Query("SELECT *FROM receipt_table WHERE date >= :dateFrom AND date <= :dateTo")
    fun getReceiptHeaders(dateFrom: Long, dateTo: Long): List<ReceiptEntity>

    @Query("SELECT id FROM receipt_table WHERE date >= :dateFrom AND date <= :dateTo ORDER BY date DESC")
    fun getMonthReceiptsId(dateFrom: Long, dateTo: Long): List<Long>

    @Query("SELECT * FROM receipt_table WHERE remoteId=:receiptId")
    fun getReceiptByRemoteId(receiptId: Long): ReceiptEntity?

    @Query("DELETE FROM receipt_table WHERE date >= :dateFrom AND date <= :dateTo")
    fun removeMonthReceipts(dateFrom: Long, dateTo: Long): Int

    @Query("DELETE FROM receipt_table WHERE id IN(:removeIds)")
    fun removeReceiptHeadersByIds(removeIds: Array<Long>): Int
}