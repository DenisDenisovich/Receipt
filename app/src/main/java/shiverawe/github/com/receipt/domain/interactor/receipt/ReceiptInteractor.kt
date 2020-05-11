package shiverawe.github.com.receipt.domain.interactor.receipt

import kotlinx.coroutines.CancellationException
import shiverawe.github.com.receipt.data.network.utils.isOnline
import shiverawe.github.com.receipt.domain.entity.ErrorType
import shiverawe.github.com.receipt.domain.entity.base.Receipt
import shiverawe.github.com.receipt.domain.entity.base.ReceiptHeader
import shiverawe.github.com.receipt.domain.entity.ReceiptResult
import shiverawe.github.com.receipt.domain.entity.ReceiptError
import shiverawe.github.com.receipt.domain.entity.base.Product
import shiverawe.github.com.receipt.domain.repository.IReceiptRepository
import java.lang.Exception

class ReceiptInteractor(private val repository: IReceiptRepository) : IReceiptInteractor {

    override suspend fun getReceipt(id: Long): ReceiptResult<Receipt> =
        try {
            ReceiptResult(repository.getReceipt(id))
        } catch (e: CancellationException) {
            ReceiptResult(isCancel = true)
        } catch (e: Exception) {
            ReceiptResult(error = getErrorResult(e))
        }

    override suspend fun getProducts(id: Long): ReceiptResult<List<Product>> =
        try {
            ReceiptResult(repository.getProducts(id))
        } catch (e: CancellationException) {
            ReceiptResult(isCancel = true)
        } catch (e: Exception) {
            ReceiptResult(error = getErrorResult(e))
        }

    override suspend fun getReceiptHeader(id: Long): ReceiptResult<ReceiptHeader> =
        try {
            ReceiptResult(repository.getReceiptHeader(id))
        } catch (e: CancellationException) {
            ReceiptResult(isCancel = true)
        } catch (e: Exception) {
            ReceiptResult(error = ReceiptError(e, ErrorType.ERROR))
        }

    private fun getErrorResult(e: Exception): ReceiptError =
        ReceiptError(e, if (isOnline()) ErrorType.ERROR else ErrorType.OFFLINE)
}