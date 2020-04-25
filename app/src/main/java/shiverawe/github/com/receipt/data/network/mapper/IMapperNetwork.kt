package shiverawe.github.com.receipt.data.network.mapper

import shiverawe.github.com.receipt.data.network.entity.item.ItemResponse
import shiverawe.github.com.receipt.data.network.entity.receipt.ReceiptResponse
import shiverawe.github.com.receipt.data.network.entity.receipt.Report
import shiverawe.github.com.receipt.domain.entity.dto.base.Receipt

interface IMapperNetwork {
    fun toReceipt(receiptResponse: List<ReceiptResponse>): ArrayList<Receipt>
    fun getToReceipt(response: ItemResponse?): Receipt?
}