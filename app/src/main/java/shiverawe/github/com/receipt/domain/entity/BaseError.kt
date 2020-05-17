package shiverawe.github.com.receipt.domain.entity

data class BaseError(
    val throwable: Throwable? = null,
    val type: ErrorType = ErrorType.ERROR,
    val message: String? = null
)