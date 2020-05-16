package shiverawe.github.com.receipt.ui.receipt.create.state

import shiverawe.github.com.receipt.domain.entity.ErrorType

data class ErrorState(
    val error: Throwable? = null,
    val type: ErrorType = ErrorType.ERROR,
    val message: String? = null
)