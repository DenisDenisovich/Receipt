package shiverawe.github.com.receipt.domain.interactor.create_receipt

import shiverawe.github.com.receipt.domain.entity.base.Meta

interface ICreateReceiptInteractor {

    suspend fun createReceipt(qrRawData: String): CreateReceiptResult

    suspend fun createReceipt(meta: Meta): CreateReceiptResult
}