package shiverawe.github.com.receipt.data.bd.room.product

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ProductDao {

    @Query("SELECT * FROM product_table WHERE receiptId IN(:receiptIds)")
    suspend fun getProductsForReceiptIds(receiptIds: Array<Long>): List<ProductEntity>

    @Insert
    suspend fun addProducts(products: List<ProductEntity>): List<Long>

    @Query("DELETE FROM product_table where id IN(:removeIds)")
    suspend fun removeProductsByIds(removeIds: Array<Long>): Int

    @Query("SELECT * FROM product_table")
    suspend fun getAllProducts(): List<ProductEntity>
}