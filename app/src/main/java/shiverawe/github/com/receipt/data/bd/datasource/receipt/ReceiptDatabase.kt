package shiverawe.github.com.receipt.data.bd.datasource.receipt

import androidx.room.Transaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import shiverawe.github.com.receipt.data.bd.mapper.toReceipt
import shiverawe.github.com.receipt.data.bd.mapper.toReceiptHeader
import shiverawe.github.com.receipt.data.bd.room.ReceiptRoom
import shiverawe.github.com.receipt.domain.entity.base.Product
import shiverawe.github.com.receipt.domain.entity.base.Receipt
import shiverawe.github.com.receipt.domain.entity.base.ReceiptHeader

class ReceiptDatabase : IReceiptDatabase {

    private val db = ReceiptRoom.getDb()

    @Transaction
    override suspend fun saveProductsToCache(remoteReceiptId: Long, products: List<Product>) =
        withContext(Dispatchers.IO) {
            val receiptDb = db.receiptDao().getReceiptByRemoteId(remoteReceiptId)
            val receiptId = receiptDb?.id ?: 0
            val localProductsDb = db.productDao().getProductsForReceiptIds(arrayOf(receiptId))
            if (localProductsDb.isEmpty()) {
                // DB doesn't contain products for this period. Save new data
                db.saveProducts(receiptId, products)
            }
        }

    @Transaction
    override suspend fun getReceiptById(remoteReceiptId: Long): Receipt? = withContext(Dispatchers.IO) {
        val receiptDb = db.receiptDao().getReceiptByRemoteId(remoteReceiptId)
        receiptDb?.id?.let { receiptDbId ->
            val productsDb = db.productDao().getProductsForReceiptIds(arrayOf(receiptDbId))
            receiptDb.toReceipt(productsDb)
        }
    }

    override suspend fun getReceiptHeaderById(remoteReceiptId: Long): ReceiptHeader? =
        withContext(Dispatchers.IO) {
            db.receiptDao().getReceiptByRemoteId(remoteReceiptId)?.toReceiptHeader()
        }
}