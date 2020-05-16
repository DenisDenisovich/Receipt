package shiverawe.github.com.receipt.data.network.mapper

import shiverawe.github.com.receipt.data.network.entity.create.CreateRequest
import shiverawe.github.com.receipt.data.network.entity.item.ItemResponse
import shiverawe.github.com.receipt.data.network.entity.receipt.ReceiptResponse
import shiverawe.github.com.receipt.domain.entity.base.Meta
import shiverawe.github.com.receipt.domain.entity.base.Product
import shiverawe.github.com.receipt.domain.entity.base.ReceiptHeader
import shiverawe.github.com.receipt.domain.entity.base.ReceiptStatus
import shiverawe.github.com.receipt.domain.entity.base.Shop
import shiverawe.github.com.receipt.utils.toStringWithSeconds
import java.text.SimpleDateFormat
import java.util.Locale

private val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale("ru"))
fun ItemResponse.toProduct(): Product = Product(text, price, amount)

fun ReceiptResponse.toReceiptHeader(): ReceiptHeader? {
    if (date == null || fd == null || fn == null || fp == null || sum == null) return null

    val dateLong = formatter.parse(date)?.time ?: 0L
    val shop = Shop(
        dateLong,
        merchantName.orEmpty(),
        merchantPlaceAddress.orEmpty(),
        sum.toString())
    val meta = Meta(dateLong, fn, fd, fp, sum)
    val status = ReceiptStatus.valueOf(status ?: return null)
    return ReceiptHeader(id, status, shop, meta)
}

fun List<ReceiptResponse>.toReceiptHeader(): List<ReceiptHeader> =
    asSequence()
        .filter {
            it.date != null &&
                it.fd != null &&
                it.fn != null &&
                it.fp != null &&
                it.sum != null &&
                it.status != null
        }
        .map { response ->
            val dateLong = formatter.parse(response.date!!)?.time ?: 0L
            val sum = response.sum!!
            val shop = Shop(
                dateLong,
                response.merchantName.orEmpty(),
                response.merchantPlaceAddress.orEmpty(),
                sum.toString()
            )
            val meta = Meta(
                dateLong,
                response.fn!!,
                response.fd!!,
                response.fp!!,
                sum
            )
            val status = ReceiptStatus.valueOf(response.status!!)
            ReceiptHeader(response.id, status, shop, meta)
        }
        .toList()

fun Meta.toCreateRequest() = CreateRequest(
    fn = fn,
    fp = fp,
    fd = fd,
    sum = s.toString(),
    date = t.toStringWithSeconds()
)