package shiverawe.github.com.receipt.data.network.mapper

import shiverawe.github.com.receipt.data.network.entity.get.ReceiptResponse
import shiverawe.github.com.receipt.data.network.entity.report.Report
import shiverawe.github.com.receipt.domain.entity.dto.base.Meta
import shiverawe.github.com.receipt.domain.entity.dto.base.Product
import shiverawe.github.com.receipt.domain.entity.dto.base.Receipt
import shiverawe.github.com.receipt.domain.entity.dto.base.Shop
import java.lang.Exception
import java.lang.NullPointerException

class MapperNetwork : IMapperNetwork {

    override fun reportToReceipt(report: ArrayList<Report>): ArrayList<Receipt> {
        val monthReceipts: ArrayList<Receipt> = ArrayList()
        report.forEach {
            val receipt = reportToReceipt(it)
            if (receipt != null) monthReceipts.add(receipt)
        }
        return monthReceipts
    }

    override fun reportToReceipt(report: Report): Receipt? {
        try {
            val products: ArrayList<Product> = ArrayList()
            report.items.forEach {
                products.add(Product(it.text!!, it.price!!, it.amount!!))
            }
            val date = report.meta.date!!.toLong() * 1000
            val place = mapShopTitle(report.meta.place!!)
            val sum: Double = report.meta.sum!!
            val fn: String = report.meta.fn!!.toString()
            val fp: String = report.meta.fp!!.toString()
            val i: String = report.meta.fd!!.toString()
            val t: String = date.toString()
            val meta = Meta(t, fn, i, fp, sum)
            val shop = Shop(date, place, sum.toString())
            return Receipt(report.meta.id!!.toLong(), shop, meta, ArrayList(products))
        } catch (e: NullPointerException) {
            e.printStackTrace()
            return null
        }
    }

    override fun getToReceipt(response: ReceiptResponse?): Receipt? {
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
        val meta = Meta(t.toString(), fn, i, fp, sum)
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