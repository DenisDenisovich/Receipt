package shiverawe.github.com.receipt.domain.interactor.create_receipt

import shiverawe.github.com.receipt.domain.entity.BaseResult
import shiverawe.github.com.receipt.domain.entity.base.Meta
import shiverawe.github.com.receipt.domain.entity.base.ReceiptHeader

interface ICreateReceiptInteractor {

    suspend fun createReceipt(qrRawData: String): BaseResult<ReceiptHeader>

    suspend fun createReceipt(meta: Meta): BaseResult<ReceiptHeader>
}