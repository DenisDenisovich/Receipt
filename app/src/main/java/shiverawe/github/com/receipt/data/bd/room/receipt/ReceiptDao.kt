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

    @Query("SELECT *from receipt_table where date >= :dateFrom and date <= :dateTo")
    fun getMonthReceipts(dateFrom: Long, dateTo: Long): List<ReceiptEntity>

    @Query("SELECT id from receipt_table where date >= :dateFrom and date <= :dateTo ORDER BY date DESC")
    fun getMonthReceiptsId(dateFrom: Long, dateTo: Long): List<Long>

    @Query("SELECT * from receipt_table where id=:receiptId")
    fun getReceiptById(receiptId: Long): ReceiptEntity

    @Query("DELETE from receipt_table where date >= :dateFrom and date <= :dateTo")
    fun removeMonthReceipts(dateFrom: Long, dateTo: Long): Int

    @Query("DELETE from receipt_table where id IN(:removeIds)")
    fun removeMonthReceiptsByIds(removeIds: Array<Long>): Int
}