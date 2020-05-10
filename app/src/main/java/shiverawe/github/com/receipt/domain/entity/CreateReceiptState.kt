package shiverawe.github.com.receipt.domain.entity

import shiverawe.github.com.receipt.domain.entity.base.ErrorType
import shiverawe.github.com.receipt.domain.entity.base.Meta
import shiverawe.github.com.receipt.domain.entity.base.ReceiptHeader

sealed class CreateReceiptState

data class CreateReceiptSuccessState(val id: Long, val meta: Meta): CreateReceiptState()

data class CreateReceiptIsExistState(val receiptHeader: ReceiptHeader): CreateReceiptState()

data class CreateReceiptErrorState(
    val error: Throwable? = null,
    val type: ErrorType = ErrorType.ERROR,
    val message: String? = null
): CreateReceiptState()

object CreateReceiptCancelTask: CreateReceiptState()