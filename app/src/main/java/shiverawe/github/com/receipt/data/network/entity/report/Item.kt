package shiverawe.github.com.receipt.data.network.entity.report

data class Item(
        val item_id: Int?,
        val receipt_id: Int?,
        val text: String?,
        val price: Double?,
        val amount: Double?,
        val name_id: Int?,
        val text_id: Any?
)