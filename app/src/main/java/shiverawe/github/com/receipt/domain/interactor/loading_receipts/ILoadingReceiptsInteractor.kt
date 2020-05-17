package shiverawe.github.com.receipt.domain.interactor.loading_receipts

import shiverawe.github.com.receipt.domain.entity.base.ReceiptHeader

interface ILoadingReceiptsInteractor {

    suspend fun getNotLoadedReceipts(): List<ReceiptHeader>
}