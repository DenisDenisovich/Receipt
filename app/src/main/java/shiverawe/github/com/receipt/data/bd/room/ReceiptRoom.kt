package shiverawe.github.com.receipt.data.bd.room

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Transaction
import shiverawe.github.com.receipt.data.bd.room.product.ProductDao
import shiverawe.github.com.receipt.data.bd.room.product.ProductEntity
import shiverawe.github.com.receipt.data.bd.room.receipt.ReceiptDao
import shiverawe.github.com.receipt.data.bd.room.receipt.ReceiptEntity
import shiverawe.github.com.receipt.data.bd.mapper.MapperDb
import shiverawe.github.com.receipt.domain.entity.dto.base.Receipt
import shiverawe.github.com.receipt.ui.App
import kotlin.collections.ArrayList

@Database(entities = [ReceiptEntity::class, ProductEntity::class], version = 1)
abstract class ReceiptRoom : RoomDatabase() {

    abstract fun receiptDao(): ReceiptDao
    abstract fun productDao(): ProductDao

    val mapper = MapperDb()

    @Transaction
    fun saveReceipts(receipts: ArrayList<Receipt>): List<Long> {
        val receiptsDb = receipts.map { mapper.receiptToDb(it) }
        val savedIds = receiptDao().addReceipts(receiptsDb)
        val savedProducts = ArrayList<ProductEntity>()
        for (receiptIndex in 0 until receipts.size) {
            receipts[receiptIndex].items.forEach { product ->
                savedProducts.add(mapper.productToDb(product, savedIds[receiptIndex]))
            }
        }
        productDao().addProducts(savedProducts)
        return savedIds
    }

    @Transaction
    fun getReceiptsWithProducts(dataFrom: Long, dataTo: Long): ArrayList<Receipt> {
        val receipts = ArrayList<Receipt>()
        var receiptsDb = receiptDao().getMonthReceipts(dataFrom, dataTo)
        receiptsDb = receiptsDb.sortedByDescending { it.date }
        val receiptIds = receiptsDb.map { it.id }.toTypedArray()
        val productsDb = productDao().getProductsForReceipts(receiptIds).sortedBy { it.receiptId }
        receiptsDb.forEach { receiptDb ->
            receipts.add(mapper.dbToReceipt(receiptDb, productsDb.filter { it.receiptId == receiptDb.id }))
        }
        return receipts
    }

    companion object {

        private var instance: ReceiptRoom? = null

        @Synchronized
        fun getDb(): ReceiptRoom {
            if (instance == null) {
                instance = Room.databaseBuilder(App.appContext,
                    ReceiptRoom::class.java,
                    "receipt.db")
                    .build()
            }
            return instance!!
        }
    }
}