package shiverawe.github.com.receipt.domain.entity

import shiverawe.github.com.receipt.domain.entity.base.ErrorType
import shiverawe.github.com.receipt.domain.entity.base.Meta

sealed class CreateReceiptState

data class CreateReceiptSuccessState(val id: Long, val meta: Meta): CreateReceiptState()

data class CreateReceiptErrorState(
    val error: Throwable? = null,
    val message: String? = null,
    val type: ErrorType = ErrorType.ERROR
): CreateReceiptState()

object CreateReceiptCancelTask: CreateReceiptState()