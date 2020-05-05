package shiverawe.github.com.receipt.domain.interactor.create_receipt

import shiverawe.github.com.receipt.domain.entity.dto.Meta

interface ICreateReceiptInteractor {

    fun createReceipt(qrRawData: String, resultListener: CreateReceiptListener)

    fun createReceipt(meta: Meta, resultListener: CreateReceiptListener)

    fun cancelWork()
}