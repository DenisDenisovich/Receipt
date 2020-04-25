package shiverawe.github.com.receipt.data.network.entity.receipt

data class ReceiptResponse(
    val id: Long,
    val date: String?,
    val fd: String?,
    val fn: String?,
    val fp: String?,
    val place: String?,
    val provider: String?,
    val status: String?,
    val sum: Double?
)