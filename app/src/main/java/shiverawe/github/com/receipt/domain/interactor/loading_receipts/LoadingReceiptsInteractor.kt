package shiverawe.github.com.receipt.domain.interactor.loading_receipts

import shiverawe.github.com.receipt.domain.entity.base.ReceiptHeader

class LoadingReceiptsInteractor: ILoadingReceiptsInteractor {

    override suspend fun getNotLoadedReceipts(): List<ReceiptHeader> {
        TODO("Not yet implemented")
    }
}