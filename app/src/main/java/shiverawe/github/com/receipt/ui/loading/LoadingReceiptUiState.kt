package shiverawe.github.com.receipt.ui.loading

import shiverawe.github.com.receipt.domain.entity.ErrorType
import shiverawe.github.com.receipt.domain.entity.base.ReceiptHeader

class LoadingReceiptUiState(
    val receipts: List<ReceiptHeader> = arrayListOf(),
    val inProgress: Boolean = false,
    val error: ErrorType? = null
)