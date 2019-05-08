package shiverawe.github.com.receipt.data.network.datasource.month

import io.reactivex.Single
import shiverawe.github.com.receipt.data.network.entity.report.ReportRequest
import shiverawe.github.com.receipt.domain.entity.receipt.base.Receipt

interface IMonthNetwork {
    fun getMonthReceipts(reportRequest: ReportRequest): Single<ArrayList<Receipt>>
}