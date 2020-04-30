package shiverawe.github.com.receipt.data.network.mapper

import shiverawe.github.com.receipt.data.network.entity.item.ItemResponse
import shiverawe.github.com.receipt.data.network.entity.receipt.ReceiptResponse
import shiverawe.github.com.receipt.domain.entity.dto.Product
import shiverawe.github.com.receipt.domain.entity.dto.ReceiptHeader

interface IMapperNetwork {

    fun toReceiptHeader(receiptResponse: List<ReceiptResponse>): List<ReceiptHeader>

    fun toProduct(response: ItemResponse): Product
}