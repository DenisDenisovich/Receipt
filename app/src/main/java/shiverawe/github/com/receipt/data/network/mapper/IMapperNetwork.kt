package shiverawe.github.com.receipt.data.network.mapper

import shiverawe.github.com.receipt.data.network.entity.item.ReceiptResponse
import shiverawe.github.com.receipt.data.network.entity.receipt.Report
import shiverawe.github.com.receipt.domain.entity.dto.base.Receipt

interface IMapperNetwork {
    fun reportToReceipt(report: ArrayList<Report>): ArrayList<Receipt>
    fun reportToReceipt(report: Report): Receipt?
    fun getToReceipt(response: ReceiptResponse?): Receipt?
}