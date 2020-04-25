package shiverawe.github.com.receipt.data.network.entity.receipt

data class Meta(
        val id: Int?,
        val status: String?,
        val sum: Double?,
        val fp: String?,
        val fn: String?,
        val fd: String?,
        val place: String?,
        val provider: String?,
        val date: Int?,
        val user: String?) {
}