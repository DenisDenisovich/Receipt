package shiverawe.github.com.receipt.domain.entity

data class ReceiptError(
    val error: Throwable? = null,
    val type: ErrorType = ErrorType.ERROR,
    val message: String? = null
)