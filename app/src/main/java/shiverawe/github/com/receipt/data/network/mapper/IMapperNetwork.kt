package shiverawe.github.com.receipt.data.network.mapper

import shiverawe.github.com.receipt.data.network.entity.get.ReceiptResponse
import shiverawe.github.com.receipt.data.network.entity.report.Report
import shiverawe.github.com.receipt.domain.entity.receipt.base.Receipt

interface IMapperNetwork {
    fun reportToReceipt(report: ArrayList<Report>): ArrayList<Receipt>
    fun reportToReceipt(report: Report): Receipt?
    fun getToReceipt(response: ReceiptResponse?): Receipt?
}