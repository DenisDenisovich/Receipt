package shiverawe.github.com.receipt.data.network.entity.report

class ReportRequest(data_from: Int, data_to: Int) {
    private val meta: Meta
    private val items = Items()

    init {
        meta = Meta(data_from, data_to)
    }

    private data class Meta(val date_from: Int, val date_to: Int)
    private class Items{}
}