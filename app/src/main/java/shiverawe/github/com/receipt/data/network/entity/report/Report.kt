package shiverawe.github.com.receipt.data.network.entity.report

data class Report(
        val fd: String?,
        val date: Int?,
        val sum: Double?,
        val user_id: Int?,
        val status: String?,
        val fp: String?,
        val fn: String?,
        val id: Int?,
        val place: String?,
        val items: List<Item>
)