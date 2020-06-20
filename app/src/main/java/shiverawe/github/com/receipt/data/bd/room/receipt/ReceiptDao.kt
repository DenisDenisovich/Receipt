package shiverawe.github.com.receipt.data.bd.room.receipt

import androidx.room.*

@Dao
interface ReceiptDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addReceipts(receipts: List<ReceiptEntity>): List<Long>

    @Update
    suspend fun updateReceipt(receipt: ReceiptEntity): Int

    @Delete
    suspend fun deleteReceipt(receipt: ReceiptEntity): Int

    @Query("SELECT *FROM receipt_table WHERE date >= :dateFrom AND date <= :dateTo")
    suspend fun getReceiptHeaders(dateFrom: Long, dateTo: Long): List<ReceiptEntity>

    @Query("SELECT id FROM receipt_table WHERE date >= :dateFrom AND date <= :dateTo ORDER BY date DESC")
    suspend fun getMonthReceiptsId(dateFrom: Long, dateTo: Long): List<Long>

    @Query("SELECT * FROM receipt_table WHERE id=:receiptId")
    suspend fun getReceiptById(receiptId: Long): ReceiptEntity?

    @Query("DELETE FROM receipt_table WHERE date >= :dateFrom AND date <= :dateTo")
    suspend fun removeMonthReceipts(dateFrom: Long, dateTo: Long): Int

    @Query("DELETE FROM receipt_table WHERE id IN(:removeIds)")
    suspend fun removeReceiptHeadersByIds(removeIds: Array<Long>): Int
}