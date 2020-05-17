package shiverawe.github.com.receipt.data.bd.datasource.loading

import shiverawe.github.com.receipt.data.bd.room.ReceiptRoom
import shiverawe.github.com.receipt.domain.entity.base.ReceiptHeader

class LoadingDatabase(private val db: ReceiptRoom): ILoadingDatabase {

    override fun addReceiptToLoading(receiptHeader: ReceiptHeader) {
        TODO("Not yet implemented")
    }

    override fun removeReceiptFromLoading(id: Long) {
        TODO("Not yet implemented")
    }

    override fun getLoadingReceipts(): List<ReceiptHeader> {
        TODO("Not yet implemented")
    }
}