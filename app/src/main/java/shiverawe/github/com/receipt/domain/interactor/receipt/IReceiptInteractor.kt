package shiverawe.github.com.receipt.domain.interactor.receipt

import shiverawe.github.com.receipt.domain.entity.base.Receipt
import shiverawe.github.com.receipt.domain.entity.base.ReceiptHeader
import shiverawe.github.com.receipt.domain.entity.ReceiptResult
import shiverawe.github.com.receipt.domain.entity.base.Product

interface IReceiptInteractor {

    suspend fun getReceipt(id: Long): ReceiptResult<Receipt>

    suspend fun getProducts(id: Long): ReceiptResult<List<Product>>

    suspend fun getReceiptHeader(id: Long): ReceiptResult<ReceiptHeader>

    fun getSharedReceipt(receipt: Receipt): String
}