package shiverawe.github.com.receipt.data.network.entity.report

data class RequestMeta(val date_from: Int, val date_to: Int)
class Items

class ReportRequest(dataFrom: Int, dataTo: Int) {
    val meta: RequestMeta = RequestMeta(dataFrom, dataTo)
    val items = Items()
}
