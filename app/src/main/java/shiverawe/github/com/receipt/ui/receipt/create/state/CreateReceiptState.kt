package shiverawe.github.com.receipt.ui.receipt.create.state

import shiverawe.github.com.receipt.domain.entity.SingleEvent
import shiverawe.github.com.receipt.domain.entity.base.ReceiptHeader

data class CreateReceiptState(
    var isWaiting: Boolean = false,
    var error: SingleEvent<ErrorState>? = null,
    val receiptHeader: ReceiptHeader? = null
)