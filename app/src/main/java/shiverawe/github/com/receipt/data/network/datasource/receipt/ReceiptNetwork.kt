package shiverawe.github.com.receipt.data.network.datasource.receipt

import shiverawe.github.com.receipt.data.network.api.Api
import shiverawe.github.com.receipt.data.network.entity.item.ItemRequest
import shiverawe.github.com.receipt.data.network.entity.receipt.ReceiptRequest
import shiverawe.github.com.receipt.data.network.mapper.toCreateRequest
import shiverawe.github.com.receipt.data.network.mapper.toProduct
import shiverawe.github.com.receipt.data.network.mapper.toReceiptHeader
import shiverawe.github.com.receipt.domain.entity.base.Meta
import shiverawe.github.com.receipt.domain.entity.base.Product
import shiverawe.github.com.receipt.domain.entity.base.Receipt
import shiverawe.github.com.receipt.domain.entity.base.ReceiptHeader

class ReceiptNetwork(private val api: Api) : IReceiptNetwork {

    override suspend fun getReceipt(id: Long): Receipt? {
        val receiptHeader = api.getReceiptsCoroutine(ReceiptRequest(ids = listOf(id)))
            .map { it.toReceiptHeader() }
            .getOrNull(0)

        return receiptHeader?.let { header ->
            val products = api.getProducts(ItemRequest(listOf(header.receiptId))).map { it.toProduct() }
            Receipt(header, products)
        }
    }

    override suspend fun getProducts(id: Long): List<Product> =
        api.getProducts(ItemRequest(receiptIds = listOf(id))).map { it.toProduct() }

    override suspend fun createReceipt(meta: Meta): ReceiptHeader =
        api.createReceipt(meta.toCreateRequest()).toReceiptHeader()!!
}