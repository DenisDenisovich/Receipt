package shiverawe.github.com.receipt.domain.interactor.receipt

import shiverawe.github.com.receipt.domain.entity.base.Receipt
import shiverawe.github.com.receipt.domain.entity.base.ReceiptHeader
import shiverawe.github.com.receipt.domain.entity.BaseResult
import shiverawe.github.com.receipt.domain.entity.base.Product
import shiverawe.github.com.receipt.domain.interactor.BaseInteractor
import shiverawe.github.com.receipt.domain.interactor.create_receipt.receipt_printer.IReceiptPrinter
import shiverawe.github.com.receipt.domain.repository.IReceiptRepository
import java.lang.Exception

class ReceiptInteractor(
    private val repository: IReceiptRepository,
    private val receiptPrinter: IReceiptPrinter
) : BaseInteractor(), IReceiptInteractor {

    override suspend fun getReceipt(id: Long): BaseResult<Receipt> =
        try {
            BaseResult(repository.getReceipt(id))
        } catch (e: Exception) {
            e.toBaseResult()
        }

    override suspend fun getProducts(id: Long): BaseResult<List<Product>> =
        try {
            BaseResult(repository.getProducts(id))
        } catch (e: Exception) {
            e.toBaseResult()
        }

    override suspend fun getReceiptHeader(id: Long): BaseResult<ReceiptHeader> =
        try {
            BaseResult(repository.getReceiptHeader(id))
        } catch (e: Exception) {
            e.toBaseResult()
        }

    override fun getSharedReceipt(receipt: Receipt): String =
        receiptPrinter.receiptToString(receipt)
}