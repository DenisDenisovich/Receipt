package shiverawe.github.com.receipt.domain.entity.base

import java.io.Serializable

data class ReceiptHeader(
    var receiptId: Long,
    val status: ReceiptStatus,
    val shop: Shop,
    val address: String,
    val meta: Meta
): Serializable