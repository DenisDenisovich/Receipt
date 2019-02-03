package shiverawe.github.com.receipt.data.network

import android.content.Context
import android.net.ConnectivityManager
import shiverawe.github.com.receipt.data.network.entity.report.Report
import shiverawe.github.com.receipt.entity.Meta
import shiverawe.github.com.receipt.entity.Product
import shiverawe.github.com.receipt.entity.Receipt
import shiverawe.github.com.receipt.entity.Shop
import shiverawe.github.com.receipt.ui.App
import java.lang.Exception
import java.lang.NullPointerException
import java.math.BigDecimal
import java.math.RoundingMode

object UtilsNetwork {

    fun mapMonth(response: ArrayList<Report>?): ArrayList<Receipt> {
        val monthReceipts: ArrayList<Receipt> = ArrayList()
        // if response is empty
        if (response == null || response.size == 0) {
            monthReceipts.clear()
            return monthReceipts
        }
        // filer response
        var report = ArrayList(response.filter { it.meta.status != null && it.meta.status != "FAILED" && it.meta.place != null && it.meta.sum != null && it.meta.date != null })
        report = ArrayList(report.sortedByDescending { it.meta.date })

        if (report.size == 0) {
            monthReceipts.clear()
            return monthReceipts
        }
        report.forEach {
            val receipt = mapReceipt(it)
            if (receipt != null) monthReceipts.add(receipt)
        }
        return monthReceipts
    }

    fun mapReceipt(report: Report): Receipt? {
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
            val shop = Shop(date, place, sum.toString() + " р")
            return Receipt(report.meta.id!!.toLong(), shop, meta, ArrayList(products))
        } catch (e: NullPointerException) {
            return null
        }
    }

    fun mapShopTitle(title: String): String {
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

    fun isOnline(): Boolean {
        val connectionManager = App.appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectionManager.activeNetworkInfo?.isConnected ?: false
    }
}