package shiverawe.github.com.receipt.data.network.datasource.month

import io.reactivex.Single
import shiverawe.github.com.receipt.data.network.api.Api
import shiverawe.github.com.receipt.data.network.entity.report.Report
import shiverawe.github.com.receipt.data.network.entity.report.ReportRequest
import shiverawe.github.com.receipt.data.network.mapper.IMapperNetwork
import shiverawe.github.com.receipt.domain.entity.dto.base.Receipt
import kotlin.collections.ArrayList

class MonthNetwork(
    private val mapper: IMapperNetwork,
    private val api: Api) : IMonthNetwork {
    override fun getMonthReceipts(reportRequest: ReportRequest): Single<ArrayList<Receipt>> {
        return api.getReceiptForMonth(reportRequest)
            .map { report ->
                val filterReport = filterMonth(report)
                mapper.reportToReceipt(filterReport)
            }
    }

    private fun filterMonth(response: ArrayList<Report>?): ArrayList<Report> {
        // if response is empty
        if (response == null || response.size == 0) {
            return ArrayList()
        }
        // filter response
        var report = ArrayList(
            response.filter {
                it.meta.status != null &&
                    it.meta.status != "FAILED" &&
                    it.meta.place != null &&
                    it.meta.sum != null &&
                    it.meta.date != null
            })
        report = ArrayList(report.sortedByDescending { it.meta.date })
        return report
    }
}