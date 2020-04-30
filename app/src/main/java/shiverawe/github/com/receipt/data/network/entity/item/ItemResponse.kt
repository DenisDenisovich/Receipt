package shiverawe.github.com.receipt.data.network.entity.item

data class ItemResponse(
    val receiptId: Int,
    val text: String,
    val price: Double,
    val amount: Double
)