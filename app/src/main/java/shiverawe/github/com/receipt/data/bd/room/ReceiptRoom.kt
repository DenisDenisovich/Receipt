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
import shiverawe.github.com.receipt.domain.entity.dto.Product
import shiverawe.github.com.receipt.domain.entity.dto.ReceiptHeader
import shiverawe.github.com.receipt.ui.App

@Database(entities = [ReceiptEntity::class, ProductEntity::class], version = 1)
abstract class ReceiptRoom : RoomDatabase() {

    abstract fun receiptDao(): ReceiptDao
    abstract fun productDao(): ProductDao

    val mapper = MapperDb()

    @Transaction
    fun saveProducts(receiptId: Long, products: List<Product>): List<Long> =
        productDao().addProducts(products.map { mapper.productToDb(it, receiptId) })

    fun saveReceiptHeaders(receipts: List<ReceiptHeader>): List<Long> {
        val receiptsDb = receipts.map { mapper.receiptHeaderToDb(it) }
        return receiptDao().addReceipts(receiptsDb)
    }

    fun getReceiptHeaders(dateFrom: Long, dateTo: Long): List<ReceiptHeader> = receiptDao()
        .getReceiptHeaders(dateFrom, dateTo)
        .map { mapper.dbToReceiptHeader(it) }

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