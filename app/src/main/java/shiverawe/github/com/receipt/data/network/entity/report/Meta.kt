package shiverawe.github.com.receipt.data.network.entity.report

data class Meta(
        val receipt_id: Int,
        val date: Int,
        val fn: String,
        val fd: String,
        val fp: String,
        val sum: Double,
        val provider: String,
        val status: String,
        val place_id: Int,
        val user_id: Any
)