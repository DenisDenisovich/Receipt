package shiverawe.github.com.receipt.data.bd.room

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Transaction
import shiverawe.github.com.receipt.data.bd.room.product.ProductDao
import shiverawe.github.com.receipt.data.bd.room.product.ProductEntity
import shiverawe.github.com.receipt.data.bd.room.receipt.ReceiptDao
import shiverawe.github.com.receipt.data.bd.room.receipt.ReceiptEntity
import shiverawe.github.com.receipt.data.bd.mapper.toReceiptHeader
import shiverawe.github.com.receipt.data.bd.mapper.toProductEntity
import shiverawe.github.com.receipt.data.bd.mapper.toReceiptEntity
import shiverawe.github.com.receipt.domain.entity.base.Product
import shiverawe.github.com.receipt.domain.entity.base.ReceiptHeader
import shiverawe.github.com.receipt.ui.App

@Database(entities = [ReceiptEntity::class, ProductEntity::class], version = 1)
abstract class ReceiptRoom : RoomDatabase() {

    abstract fun receiptDao(): ReceiptDao
    abstract fun productDao(): ProductDao

    @Transaction
    suspend fun saveProducts(receiptId: Long, products: List<Product>): List<Long> =
        productDao().addProducts(products.map { it.toProductEntity(receiptId) })

    suspend fun saveReceiptHeaders(receipts: List<ReceiptHeader>): List<Long> {
        val receiptsDb = receipts.map { it.toReceiptEntity() }
        return receiptDao().addReceipts(receiptsDb)
    }

    suspend fun getReceiptHeaders(dateFrom: Long, dateTo: Long): List<ReceiptHeader> = receiptDao()
        .getReceiptHeaders(dateFrom, dateTo)
        .map { it.toReceiptHeader() }

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