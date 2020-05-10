package shiverawe.github.com.receipt.domain.interactor.receipt

import kotlinx.coroutines.CancellationException
import shiverawe.github.com.receipt.data.network.utils.isOnline
import shiverawe.github.com.receipt.domain.entity.ErrorType
import shiverawe.github.com.receipt.domain.entity.base.Receipt
import shiverawe.github.com.receipt.domain.entity.base.ReceiptHeader
import shiverawe.github.com.receipt.domain.entity.Result
import shiverawe.github.com.receipt.domain.entity.ResultError
import shiverawe.github.com.receipt.domain.repository.IReceiptRepository
import java.lang.Exception

class ReceiptInteractor(private val repository: IReceiptRepository) : IReceiptInteractor {

    override suspend fun getReceipt(id: Long): Result<Receipt> =
        try {
            Result(repository.getReceipt(id))
        } catch (e: CancellationException) {
            Result(isCancel = true)
        } catch (e: Exception) {
            Result(error = getErrorResult(e))
        }

    override suspend fun getReceipt(receiptHeader: ReceiptHeader): Result<Receipt> =
        try {
            val products = repository.getProducts(receiptHeader.receiptId)
            Result(Receipt(receiptHeader, products))
        } catch (e: CancellationException) {
            Result(isCancel = true)
        } catch (e: Exception) {
            Result(error = getErrorResult(e))
        }

    override suspend fun getReceiptHeader(id: Long): Result<ReceiptHeader> =
        try {
            Result(repository.getReceiptHeader(id))
        } catch (e: CancellationException) {
            Result(isCancel = true)
        } catch (e: Exception) {
            Result(error = ResultError(e, ErrorType.ERROR))
        }

    private fun getErrorResult(e: Exception): ResultError =
        ResultError(e, if (isOnline()) ErrorType.ERROR else ErrorType.OFFLINE)
}