package shiverawe.github.com.receipt.ui.history.month

import shiverawe.github.com.receipt.domain.entity.base.ReceiptHeader

data class MonthUiState(
    val receipts: List<ReceiptHeader> = arrayListOf(),
    val sum: String = "",
    val message: MessageType? = null,
    val inProgress: Boolean = false
)