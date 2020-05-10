package shiverawe.github.com.receipt.domain.interactor.receipt

data class ReceiptResult<T>(
    val result: T? = null,
    val error: ReceiptErrorResult? = null,
    val isCancel: Boolean = false
)
