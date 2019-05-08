package shiverawe.github.com.receipt.data.network.mapper

import shiverawe.github.com.receipt.data.network.entity.get.ReceiptResponse
import shiverawe.github.com.receipt.data.network.entity.report.Report
import shiverawe.github.com.receipt.domain.entity.receipt.base.Meta
import shiverawe.github.com.receipt.domain.entity.receipt.base.Product
import shiverawe.github.com.receipt.domain.entity.receipt.base.Receipt
import shiverawe.github.com.receipt.domain.entity.receipt.base.Shop
import java.lang.Exception
import java.lang.NullPointerException
import java.math.BigDecimal
import java.math.RoundingMode

class MapperNetwork: IMapperNetwork {

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
            val sum = BigDecimal(report.meta.sum!!).setScale(2, RoundingMode.DOWN).toDouble()
            val fn = report.meta.fn.toString()
            val fp = report.meta.fp.toString()
            val i = report.meta.fd.toString()
            val t = date.toString()
            val meta = Meta(t, fn, i, fp, sum)
            val shop = Shop(date, place, sum.toString())
            return Receipt(report.meta.id!!.toLong(), shop, meta, ArrayList(products))
        } catch (e: NullPointerException) {
            return null
        }
    }

    override fun getToReceipt(response: ReceiptResponse?): Receipt? {
        if (response?.meta == null || response.items == null) return null
        val products = java.util.ArrayList<Product>()
        response.items.forEach {
            products.add(Product(it.text
                    ?: "", it.price ?: 0.0, it.amount ?: 0.0))
        }
        val fn = response.meta.fn.toString()
        val fp = response.meta.fp.toString()
        val i = response.meta.fd.toString()
        val t = response.meta.date!!.toLong() * 1000
        val sum = BigDecimal(response.meta.sum).setScale(2, RoundingMode.DOWN).toDouble()
        val meta = Meta(t.toString(), fn, i, fp, sum)
        val shopPlace = mapShopTitle(response.meta.place ?: "")
        val shop = Shop(t, shopPlace, "$sum р")
        return Receipt(0, shop, meta, products)
    }

    private fun mapShopTitle(title: String): String {
        try {
            if (title.contains('\"')) {
                return title.substring(title.indexOf('\"', 0) + 1, title.lastIndexOf('\"', title.length))
            } else if (title.contains("ООО")) {
                return title.split("ООО")[1].trim()
            }
        } catch (e: Exception) {
        }
        return title
    }

}