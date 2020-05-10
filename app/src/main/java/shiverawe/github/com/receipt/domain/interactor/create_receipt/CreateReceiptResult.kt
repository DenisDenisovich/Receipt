package shiverawe.github.com.receipt.domain.interactor.create_receipt

import shiverawe.github.com.receipt.domain.entity.base.ErrorType
import shiverawe.github.com.receipt.domain.entity.base.Meta
import shiverawe.github.com.receipt.domain.entity.base.ReceiptHeader

// TODO: refactor. Create class like [ReceiptResult]
sealed class CreateReceiptResult

data class CreateReceiptSuccessResult(val id: Long, val meta: Meta): CreateReceiptResult()

data class CreateReceiptIsExistResult(val receiptHeader: ReceiptHeader): CreateReceiptResult()

data class CreateReceiptErrorResult(
    val error: Throwable? = null,
    val type: ErrorType = ErrorType.ERROR,
    val message: String? = null
): CreateReceiptResult()

object CreateReceiptCancelTaskResult: CreateReceiptResult()