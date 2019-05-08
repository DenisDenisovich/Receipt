package shiverawe.github.com.receipt.data.network

import io.reactivex.Single
import shiverawe.github.com.receipt.data.network.entity.report.Report
import shiverawe.github.com.receipt.data.network.entity.report.ReportRequest
import shiverawe.github.com.receipt.data.network.mapper.MapperNetwork
import shiverawe.github.com.receipt.domain.entity.receipt.base.Receipt
import shiverawe.github.com.receipt.ui.App
import kotlin.collections.ArrayList

class MonthNetwork: IMonthNetwork {

    private val mapper = MapperNetwork()
    override fun getMonthReceipts(reportRequest: ReportRequest): Single<ArrayList<Receipt>> {
        return App.api.getReceiptForMonth(reportRequest)
                .map { report ->
                    val filterReport = filterMonth(report)
                    mapper.reportToReceipt(filterReport)
                }
    }

    fun filterMonth(response: ArrayList<Report>?): ArrayList<Report> {
        // if response is empty
        if (response == null || response.size == 0) {
            return ArrayList()
        }
        // filter response
        var report = ArrayList(response.filter { it.meta.status != null && it.meta.status != "FAILED" && it.meta.place != null && it.meta.sum != null && it.meta.date != null })
        report = ArrayList(report.sortedByDescending { it.meta.date })
        return report
    }
}