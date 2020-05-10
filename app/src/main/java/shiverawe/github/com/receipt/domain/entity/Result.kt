package shiverawe.github.com.receipt.domain.entity

data class Result<T>(
    val result: T? = null,
    val error: ResultError? = null,
    val isCancel: Boolean = false
)
