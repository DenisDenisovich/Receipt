package shiverawe.github.com.receipt.data.network.entity.receipt

data class Receipt(
    val id: Long,
    val data: String,
    val sum: String,
    val status: String,
    val place: String
)