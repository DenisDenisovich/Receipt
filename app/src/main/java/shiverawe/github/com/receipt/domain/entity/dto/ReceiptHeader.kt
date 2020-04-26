package shiverawe.github.com.receipt.domain.entity.dto

data class ReceiptHeader(
    var receiptId: Long,
    val shop: Shop,
    val meta: Meta
)