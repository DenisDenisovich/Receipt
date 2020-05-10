package shiverawe.github.com.receipt.domain.interactor.receipt

import shiverawe.github.com.receipt.domain.entity.base.Receipt
import shiverawe.github.com.receipt.domain.entity.base.ReceiptHeader
import shiverawe.github.com.receipt.domain.repository.IReceiptRepository

class ReceiptInteractor(private val repository: IReceiptRepository): IReceiptInteractor {

    override suspend fun getReceipt(id: Long): Receipt? = repository.getReceipt(id)

    override suspend fun getReceipt(receiptHeader: ReceiptHeader): Receipt {
        val products = repository.getProducts(receiptHeader.receiptId)

        return Receipt(receiptHeader, products)
    }

    override suspend fun getReceiptHeader(id: Long): ReceiptHeader? = repository.getReceiptHeader(id)
}