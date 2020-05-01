package shiverawe.github.com.receipt.data.network.mapper

import shiverawe.github.com.receipt.data.network.entity.item.ItemResponse
import shiverawe.github.com.receipt.data.network.entity.receipt.ReceiptResponse
import shiverawe.github.com.receipt.domain.entity.dto.Meta
import shiverawe.github.com.receipt.domain.entity.dto.Product
import shiverawe.github.com.receipt.domain.entity.dto.ReceiptHeader
import shiverawe.github.com.receipt.domain.entity.dto.Shop
import java.text.SimpleDateFormat
import java.util.*

private val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())

fun ItemResponse.toProduct(): Product = Product(text, price, amount)

fun List<ReceiptResponse>.toReceiptHeader(): List<ReceiptHeader> =
        this
                .asSequence()
                .filter {
                    it.date != null &&
                            it.fd != null &&
                            it.fn != null &&
                            it.fp != null &&
                            it.place != null &&
                            it.provider != null &&
                            it.status != null &&
                            it.sum != null
                }
                .map { response ->
                    val date = formatter.parse(response.date!!)?.time ?: 0L
                    val sum = response.sum!!
                    val shop = Shop(date, response.place!!, sum.toString())
                    val meta = Meta(
                            date,
                            response.fn!!,
                            response.fd!!,
                            response.fp!!,
                            sum
                    )
                    ReceiptHeader(response.id, shop, meta)
                }.toList()
