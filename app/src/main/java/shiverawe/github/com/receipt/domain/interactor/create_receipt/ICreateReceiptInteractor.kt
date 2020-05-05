package shiverawe.github.com.receipt.domain.interactor.create_receipt

import shiverawe.github.com.receipt.domain.entity.CreateReceiptState
import shiverawe.github.com.receipt.domain.entity.base.Meta

interface ICreateReceiptInteractor {

    suspend fun createReceipt(qrRawData: String): CreateReceiptState

    suspend fun createReceipt(meta: Meta): CreateReceiptState
}