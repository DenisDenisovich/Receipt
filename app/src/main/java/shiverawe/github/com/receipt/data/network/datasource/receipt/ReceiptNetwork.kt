package shiverawe.github.com.receipt.data.network.datasource.receipt

import io.reactivex.Single
import shiverawe.github.com.receipt.data.network.api.Api
import shiverawe.github.com.receipt.data.network.entity.create.CreateRequest
import shiverawe.github.com.receipt.data.network.entity.create.CreateResponse
import shiverawe.github.com.receipt.data.network.entity.item.ItemRequest
import shiverawe.github.com.receipt.data.network.entity.receipt.ReceiptRequest
import shiverawe.github.com.receipt.data.network.mapper.IMapperNetwork
import shiverawe.github.com.receipt.domain.entity.dto.Meta
import shiverawe.github.com.receipt.domain.entity.dto.Product
import shiverawe.github.com.receipt.domain.entity.dto.Receipt
import java.text.SimpleDateFormat
import java.util.*

class ReceiptNetwork(
    private val mapper: IMapperNetwork,
    private val api: Api
) : IReceiptNetwork {

    private val getReceiptDateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    private val createReceiptDateFormatter = SimpleDateFormat("yyyyMMdd'T'HHmm", Locale.getDefault())

    override fun getReceipt(meta: Meta): Single<Receipt?> =
        api.getReceipts(
            ReceiptRequest(
                date = getReceiptDateFormatter.format(meta.t),
                fn = meta.fn,
                fp = meta.fp,
                fd = meta.fd,
                sum = meta.s.toString()
            )
        ).flatMap { receiptResponse ->
            if (receiptResponse.isNotEmpty()) {
                val receiptHeader = mapper.toReceiptHeader(receiptResponse)[0]
                getProducts(receiptHeader.receiptId).map { products ->
                    Receipt(receiptHeader, products)
                }
            } else {
                Single.error<Receipt>(Throwable(""))
            }
        }

    override fun getProducts(id: Long): Single<List<Product>> =
        api.getProducts(ItemRequest(receiptIds = listOf(id))).map { productResponse ->
            productResponse.map { mapper.toProduct(it) }
        }

    override fun saveReceipt(meta: Meta): Single<Long> = api.createReceipt(
            CreateRequest(
                fn = meta.fn,
                fp = meta.fp,
                fd = meta.fd,
                sum = meta.s.toString(),
                date = createReceiptDateFormatter.format(meta.t)
            )
        ).map { it.id }
}