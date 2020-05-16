package shiverawe.github.com.receipt.data.network.entity.receipt

data class ReceiptResponse(
    val id: Long,
    val fd: String?,
    val fn: String?,
    val fp: String?,
    val date: String?,
    val sum: Double?,
    val merchantName: String?,
    val merchantPlaceAddress: String?,
    val status: String?
)