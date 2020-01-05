package shiverawe.github.com.receipt.domain.repository

import io.reactivex.Single
import shiverawe.github.com.receipt.data.network.entity.report.ReportRequest
import shiverawe.github.com.receipt.domain.entity.dto.month.ReceiptMonth

interface IMonthRepository {
    fun getMonthReceipt(reportRequest: ReportRequest): Single<ArrayList<ReceiptMonth>>
}
