package shiverawe.github.com.receipt.data.network.mapper

import shiverawe.github.com.receipt.data.network.entity.item.ItemResponse
import shiverawe.github.com.receipt.data.network.entity.receipt.ReceiptResponse
import shiverawe.github.com.receipt.domain.entity.dto.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MapperNetwork : IMapperNetwork {

    private val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())

    override fun toReceiptHeader(receiptResponse: List<ReceiptResponse>): ArrayList<ReceiptHeader> =
        receiptResponse
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
            }
            .toCollection(ArrayList())

    override fun getToReceipt(response: ItemResponse?): Receipt? {
        if (response?.meta == null || response.items == null) return null
        val products = ArrayList<Product>()
        response.items.forEach {
            products.add(
                Product(
                    it.text ?: "",
                    it.price ?: 0.0,
                    it.amount ?: 0.0
                )
            )
        }
        val fn = response.meta.fn!!.toString()
        val fp = response.meta.fp!!.toString()
        val i = response.meta.fd!!.toString()
        val t = response.meta.date!!.toLong() * 1000
        val sum = response.meta.sum!!.toDouble()
        val meta = Meta(t, fn, i, fp, sum)
        val shopPlace = mapShopTitle(response.meta.place ?: "")
        val shop = Shop(t, shopPlace, "$sum р")
        return Receipt(0, shop, meta, products)
    }

    private fun mapShopTitle(title: String): String {
        try {
            if (title.contains('\"')) {
                return title.substring(
                    title.indexOf('\"', 0) + 1,
                    title.lastIndexOf('\"', title.length)
                )
            } else if (title.contains("ООО")) {
                return title.split("ООО")[1].trim()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return title
    }
}