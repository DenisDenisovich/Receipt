package shiverawe.github.com.receipt.domain.entity

data class BaseResult<T>(
    val result: T? = null,
    val error: BaseError? = null,
    val isCancel: Boolean = false
) {
    constructor(result: T?, throwable: Throwable?, type: ErrorType): this(result, BaseError(throwable, type))
    constructor(throwable: Throwable?, type: ErrorType): this(error = BaseError(throwable, type))
    constructor(type: ErrorType): this(error = BaseError(type = type))
}
