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

class ReceiptDatabase(private val db: ReceiptRoom) : IReceiptDatabase {

    @Transaction
    override suspend fun saveProductsToCache(receiptId: Long, products: List<Product>) =
        withContext(Dispatchers.IO) {
            val localProductsDb = db.productDao().getProductsForReceiptIds(arrayOf(receiptId))
            if (localProductsDb.isEmpty()) {
                // DB doesn't contain products for this period. Save new data
                db.saveProducts(receiptId, products)
            }
        }

    @Transaction
    override suspend fun getReceiptById(receiptId: Long): Receipt? = withContext(Dispatchers.IO) {
        val receiptDb = db.receiptDao().getReceiptById(receiptId)
        receiptDb?.id?.let { receiptDbId ->
            val productsDb = db.productDao().getProductsForReceiptIds(arrayOf(receiptDbId))
            receiptDb.toReceipt(productsDb)
        }
    }

    override suspend fun getReceiptHeaderById(receiptId: Long): ReceiptHeader? =
        withContext(Dispatchers.IO) {
            db.receiptDao().getReceiptById(receiptId)?.toReceiptHeader()
        }
}