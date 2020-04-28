package shiverawe.github.com.receipt.data.network.entity.receipt

data class ReceiptRequest(
    val dateFrom: String? = null,
    val dateTo: String? = null,
    val date: String? = null,
    val fn: String? = null,
    val fd: String? = null,
    val fp: String? = null,
    val sum: String? = null
)