package shiverawe.github.com.receipt.domain.interactor.receipt

import shiverawe.github.com.receipt.domain.entity.base.ErrorType

data class ReceiptErrorResult(
    val error: Throwable? = null,
    val type: ErrorType = ErrorType.ERROR,
    val message: String? = null
)