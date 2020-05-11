package shiverawe.github.com.receipt.domain.entity

data class ReceiptResult<T>(
    val result: T? = null,
    val error: ReceiptError? = null,
    val isCancel: Boolean = false
)
