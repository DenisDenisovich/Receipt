package shiverawe.github.com.receipt.domain.repository

import io.reactivex.Single
import shiverawe.github.com.receipt.data.network.entity.receipt.ReportRequest
import shiverawe.github.com.receipt.domain.entity.dto.month.ReceiptMonth

interface IMonthRepository {
    fun getMonthReceipt(dateFrom: Long, dateTo: Long): Single<ArrayList<ReceiptMonth>>
}