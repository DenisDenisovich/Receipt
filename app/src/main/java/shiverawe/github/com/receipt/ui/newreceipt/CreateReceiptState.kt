package shiverawe.github.com.receipt.ui.newreceipt

import shiverawe.github.com.receipt.domain.entity.dto.Meta

sealed class CreateReceiptState

data class ErrorState(
    val error: Throwable? = null,
    val message: String? = null,
    val type: ErrorType = ErrorType.DEFAULT
) : CreateReceiptState()

data class SuccessState(val date: Long) : CreateReceiptState()

object ExitState : CreateReceiptState()

data class QrCodeState(
    var isWaiting: Boolean = false,
    var error: ErrorState? = null
) : CreateReceiptState()

data class ManualState(
    val meta: Meta = Meta(),
    var isWaiting: Boolean = false,
    var error: ErrorState? = null
) : CreateReceiptState()