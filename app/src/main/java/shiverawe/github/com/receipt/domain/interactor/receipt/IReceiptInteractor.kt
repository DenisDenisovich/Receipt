package shiverawe.github.com.receipt.domain.interactor.receipt

import shiverawe.github.com.receipt.domain.entity.base.Receipt
import shiverawe.github.com.receipt.domain.entity.base.ReceiptHeader

interface IReceiptInteractor {

    suspend fun getReceipt(id: Long): Receipt?

    suspend fun getReceipt(receiptHeader: ReceiptHeader): Receipt

    suspend fun getReceiptHeader(id: Long): ReceiptHeader?
}