package shiverawe.github.com.receipt.domain.interactor.receipt

import shiverawe.github.com.receipt.domain.entity.base.Receipt
import shiverawe.github.com.receipt.domain.entity.base.ReceiptHeader
import shiverawe.github.com.receipt.domain.entity.BaseResult
import shiverawe.github.com.receipt.domain.entity.base.Product

interface IReceiptInteractor {

    suspend fun getReceipt(id: Long): BaseResult<Receipt>

    suspend fun getProducts(id: Long): BaseResult<List<Product>>

    suspend fun getReceiptHeader(id: Long): BaseResult<ReceiptHeader>

    fun getSharedReceipt(receipt: Receipt): String
}