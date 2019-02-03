package shiverawe.github.com.receipt.data.bd.product

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

@Dao
interface ProductDao {

    @Query("SELECT * FROM product_table WHERE receiptId IN(:receiptIds)")
    fun getProductsForReceipts(receiptIds: Array<Long?>): List<ProductEntity>
    @Insert
    fun addProducts(products: List<ProductEntity>): List<Long>

    @Query("SELECT * FROM product_table")
    fun getAllProducts(): List<ProductEntity>
}