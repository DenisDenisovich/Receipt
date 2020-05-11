package shiverawe.github.com.receipt.ui.receipt.create

import shiverawe.github.com.receipt.domain.entity.base.Meta
import shiverawe.github.com.receipt.domain.entity.ErrorType
import shiverawe.github.com.receipt.domain.entity.base.ReceiptHeader

sealed class CreateReceiptUiState

data class ErrorState(
    val error: Throwable? = null,
    val message: String? = null,
    val type: ErrorType = ErrorType.ERROR
) : CreateReceiptUiState()

data class SuccessState(val date: Long) : CreateReceiptUiState()

data class ShowReceiptState(val receiptHeader: ReceiptHeader): CreateReceiptUiState()

object ExitState : CreateReceiptUiState()

data class QrCodeState(
    var isWaiting: Boolean = false,
    var error: ErrorState? = null
) : CreateReceiptUiState()

data class ManualState(
    val meta: Meta = Meta(),
    var isWaiting: Boolean = false,
    var error: ErrorState? = null,
    val isFirstScreen: Boolean = false
) : CreateReceiptUiState()