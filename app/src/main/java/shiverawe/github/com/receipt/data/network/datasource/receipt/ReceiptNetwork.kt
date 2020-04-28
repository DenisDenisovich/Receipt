package shiverawe.github.com.receipt.data.network.datasource.receipt

import io.reactivex.Single
import shiverawe.github.com.receipt.data.network.api.Api
import shiverawe.github.com.receipt.data.network.entity.create.CreateRequest
import shiverawe.github.com.receipt.data.network.entity.create.CreateResponce
import shiverawe.github.com.receipt.data.network.entity.item.ItemRequest
import shiverawe.github.com.receipt.data.network.entity.receipt.ReceiptRequest
import shiverawe.github.com.receipt.data.network.mapper.IMapperNetwork
import shiverawe.github.com.receipt.domain.entity.dto.Meta
import shiverawe.github.com.receipt.domain.entity.dto.Product
import shiverawe.github.com.receipt.domain.entity.dto.Receipt
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ReceiptNetwork(
    private val mapper: IMapperNetwork,
    private val api: Api) : IReceiptNetwork {

    private val timeFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())

    private var parameters: Map<String, String>? = null

    override fun getReceipt(meta: Meta): Single<Receipt?> =
        api.getReceipts(
            ReceiptRequest(
                date = timeFormatter.format(meta.t),
                fn = meta.fn,
                fp = meta.fp,
                fd = meta.fd,
                sum = meta.s.toString()
            )
        ).flatMap { receiptResponse ->
            if (receiptResponse.isNotEmpty()) {
                val receiptHeader = mapper.toReceiptHeader(receiptResponse)[0]
                getProducts(receiptHeader.receiptId).map { products ->
                    Receipt(receiptHeader.receiptId, receiptHeader.shop, receiptHeader.meta, products)
                }
            } else {
                Single.error<Receipt>(Throwable(""))
            }
        }

    override fun getProducts(id: Long): Single<ArrayList<Product>> =
        api.getProducts(ItemRequest(receiptIds = listOf(id))).map { productResponse ->
            productResponse.map { mapper.toProduct(it) }.toCollection(ArrayList())
        }

    override fun saveReceipt(): Single<CreateResponce> {
        val fn = parameters?.get("fn") ?: ""
        val fp = parameters?.get("fn") ?: ""
        val i = parameters?.get("fn") ?: ""
        val s = parameters?.get("fn") ?: ""
        val t = parameters?.get("fn") ?: ""
        val createRequest = CreateRequest(fn, fp, i, s, t)
        return api.createReceipt(createRequest)
    }
}