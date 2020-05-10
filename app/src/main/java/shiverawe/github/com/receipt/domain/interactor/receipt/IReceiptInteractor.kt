package shiverawe.github.com.receipt.domain.interactor.receipt

import shiverawe.github.com.receipt.domain.entity.base.Receipt
import shiverawe.github.com.receipt.domain.entity.base.ReceiptHeader
import shiverawe.github.com.receipt.domain.entity.Result

interface IReceiptInteractor {

    suspend fun getReceipt(id: Long): Result<Receipt>

    suspend fun getReceipt(receiptHeader: ReceiptHeader): Result<Receipt>

    suspend fun getReceiptHeader(id: Long): Result<ReceiptHeader>
}