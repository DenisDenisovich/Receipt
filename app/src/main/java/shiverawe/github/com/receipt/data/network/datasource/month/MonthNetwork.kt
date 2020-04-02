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
        private val api: Api
) : IMonthNetwork {

    override fun getMonthReceipts(reportRequest: ReportRequest): Single<ArrayList<Receipt>> {
        return api.getReceiptForMonth(reportRequest)
                .map { report -> mapper.reportToReceipt(filterMonth(report)) }
    }

    private fun filterMonth(response: List<Report>?): ArrayList<Report> {
        // if response is empty
        if (response.isNullOrEmpty()) {
            return ArrayList()
        }

        // filter response
        val report = response
                .filter {
                    it.meta.status != null
                            && it.meta.status != "FAILED"
                            && it.meta.place != null
                            && it.meta.sum != null
                            && it.meta.date != null
                }
                .sortedByDescending { it.meta.date }

        return ArrayList(report) // TODO why ArrayList, not List\MutableList?
    }

}
