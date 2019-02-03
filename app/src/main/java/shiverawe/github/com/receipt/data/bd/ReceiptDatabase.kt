package shiverawe.github.com.receipt.data.bd

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.Transaction
import android.util.Log
import shiverawe.github.com.receipt.data.bd.product.ProductDao
import shiverawe.github.com.receipt.data.bd.product.ProductEntity
import shiverawe.github.com.receipt.data.bd.receipt.ReceiptDao
import shiverawe.github.com.receipt.data.bd.receipt.ReceiptEntity
import shiverawe.github.com.receipt.data.network.entity.report.ReportRequest
import shiverawe.github.com.receipt.entity.Receipt
import shiverawe.github.com.receipt.ui.App
import java.util.*
import kotlin.collections.ArrayList

@Database(entities = [ReceiptEntity::class, ProductEntity::class], version = 1)
abstract class ReceiptDatabase: RoomDatabase() {

    abstract fun receiptDao(): ReceiptDao
    abstract fun productDao(): ProductDao
    companion object {
        private var instance: ReceiptDatabase? = null
        fun getDb(): ReceiptDatabase {
            if (instance == null) {
                synchronized(ReceiptDatabase::class) {
                    instance = Room.databaseBuilder(App.appContext,
                            ReceiptDatabase::class.java,
                            "receipt.db")
                            .build()
                }
            }
            return instance!!
        }
    }


    fun updateMonthCache(reportRequest: ReportRequest, networkReceipts: ArrayList<Receipt>): ArrayList<Receipt> {
        val dateFrom = reportRequest.meta.date_from.toLong() * 1000L
        val dateTo = reportRequest.meta.date_to.toLong() * 1000L
        val localReceipts = getReceiptsWithProducts(dateFrom, dateTo)
        val localIds: List<Long>
        if (localReceipts.size == 0) {
            localIds = saveReceipts(networkReceipts)
        } else {
            val (deletedIds, newNetwork) = UtilsDB.findDiffReceipts(localReceipts, networkReceipts)
            receiptDao().removeMonthReceiptsByIds(deletedIds.toTypedArray())
            saveReceipts(newNetwork)
            localIds = receiptDao().getMonthReceiptsId(dateFrom, dateTo)
        }
        val receipts = ArrayList<Receipt>()
        for (i in 0 until networkReceipts.size) {
            receipts.add(Receipt(localIds[i], networkReceipts[i].shop, networkReceipts[i].meta, ArrayList()))
        }
        return receipts
    }

    @Transaction
    fun saveReceipts(receipts: ArrayList<Receipt>): List<Long>{
        val receiptsDb = receipts.map { it ->UtilsDB.mapReceiptToDb(it) }
        val savedIds = receiptDao().addReceipts(receiptsDb)
        val savedProducts = ArrayList<ProductEntity>()
        for (receiptIndex in 0 until receipts.size){
            receipts[receiptIndex].items.forEach {
                product ->
                savedProducts.add(UtilsDB.mapProductToDb(product, savedIds[receiptIndex]))
            }
        }
        productDao().addProducts(savedProducts)
        return savedIds
    }

    fun getReceipts(dataFrom: Long, dataTo: Long): ArrayList<Receipt> {
        val receipts = ArrayList<Receipt>()
        var receiptsDb = receiptDao().getMonthReceipts(dataFrom, dataTo)
        receiptsDb = receiptsDb.sortedByDescending { it.date }
        receiptsDb.forEach {
            receiptDb ->
            receipts.add(UtilsDB.mapFromDb(receiptDb, ArrayList()))
        }
        return receipts
    }

    @Transaction
    fun getReceiptsWithProducts(dataFrom: Long, dataTo: Long): ArrayList<Receipt> {
        val receipts = ArrayList<Receipt>()
        var receiptsDb = receiptDao().getMonthReceipts(dataFrom, dataTo)
        receiptsDb = receiptsDb.sortedByDescending { it.date }
        val receiptIds = receiptsDb.map { it.id }.toTypedArray()
        val productsDb = productDao().getProductsForReceipts(receiptIds).sortedBy { it.receiptId }
        receiptsDb.forEach {
            receiptDb ->
            receipts.add(UtilsDB.mapFromDb(receiptDb, productsDb.filter { it.receiptId == receiptDb.id }))
        }
        return receipts
    }

    @Transaction
    fun getReceiptById(receiptId: Long): Receipt {
        val receipt = receiptDao().getReceiptById(receiptId)
        val products = productDao().getProductsForReceipts(arrayOf(receiptId))
        return UtilsDB.mapFromDb(receipt, products)
    }
}