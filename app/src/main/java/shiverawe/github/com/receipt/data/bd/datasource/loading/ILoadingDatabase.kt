package shiverawe.github.com.receipt.data.bd.datasource.loading

import shiverawe.github.com.receipt.domain.entity.base.ReceiptHeader

interface ILoadingDatabase {

    fun addReceiptToLoading(receiptHeader: ReceiptHeader)

    fun removeReceiptFromLoading(id: Long)

    fun getLoadingReceipts(): List<ReceiptHeader>
}