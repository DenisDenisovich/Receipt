package shiverawe.github.com.receipt.data.repository

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import shiverawe.github.com.receipt.data.bd.ReceiptDatabase
import shiverawe.github.com.receipt.data.network.UtilsNetwork
import shiverawe.github.com.receipt.data.network.entity.create.CreateRequest
import shiverawe.github.com.receipt.data.network.entity.create.CreateResponce
import shiverawe.github.com.receipt.data.network.entity.get.ReceiptResponce
import shiverawe.github.com.receipt.entity.Meta
import shiverawe.github.com.receipt.entity.Product
import shiverawe.github.com.receipt.entity.Receipt
import shiverawe.github.com.receipt.entity.Shop
import shiverawe.github.com.receipt.ui.App
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.ArrayList

class ReceiptRepository {
    private val db = ReceiptDatabase.getDb()
    private var parameters: Map<String, String>? = null

    fun getReceipt(options: Map<String, String>): Single<Receipt?> {
        parameters = options
        return App.api.getReceipt(options).map { it -> map(it) }
    }

    private fun map(response: ReceiptResponce?): Receipt? {
        if (response?.meta == null || response.items == null) return null
        val products = ArrayList<Product>()
        response.items.forEach {
            products.add(Product(it.text ?: "", it.price?: 0.0, it.amount ?: 0.0))
        }
        val fn = response.meta.fn.toString()
        val fp = response.meta.fp.toString()
        val i = response.meta.fd.toString()
        val t = response.meta.date!!.toLong() * 1000
        val sum = BigDecimal(response.meta.sum).setScale(2, RoundingMode.DOWN).toDouble()
        val meta = Meta(t.toString(), fn, i, fp, sum)
        val shopPlace = UtilsNetwork.mapShopTitle(response.meta.place ?: "")
        val shop = Shop(t, shopPlace, "$sum р")
        return Receipt(0, shop, meta, products)
    }

    fun saveReceipt(): Single<CreateResponce> {
        val fn = parameters?.get("fn") ?: ""
        val fp = parameters?.get("fn") ?: ""
        val i = parameters?.get("fn") ?: ""
        val s = parameters?.get("fn") ?: ""
        val t = parameters?.get("fn") ?: ""
        val createRequest = CreateRequest(fn, fp, i, s, t)
        return App.api.createReceipt(createRequest)
    }

    fun getReceiptById(receiptId: Long): Single<Receipt> {
        return Single.create<Receipt> { emitter ->
            emitter.onSuccess(db.getReceiptById(receiptId))
        }.subscribeOn(Schedulers.io())
    }
}