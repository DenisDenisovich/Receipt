package shiverawe.github.com.receipt.data.bd.room.product

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ProductDao {

    @Query("SELECT * FROM product_table WHERE receiptId IN(:receiptIds)")
    fun getProductsForReceiptIds(receiptIds: Array<Long?>): List<ProductEntity>

    @Insert
    fun addProducts(products: List<ProductEntity>): List<Long>

    @Query("DELETE FROM product_table where id IN(:removeIds)")
    fun removeProductsByIds(removeIds: Array<Long>): Int

    @Query("SELECT * FROM product_table")
    fun getAllProducts(): List<ProductEntity>
}