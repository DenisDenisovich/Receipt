package shiverawe.github.com.receipt.data.repository

import io.reactivex.Single
import shiverawe.github.com.receipt.data.network.entity.create.CreateRequest
import shiverawe.github.com.receipt.data.network.entity.create.CreateResponce
import shiverawe.github.com.receipt.data.network.entity.get.ReceiptResponce
import shiverawe.github.com.receipt.entity.Meta
import shiverawe.github.com.receipt.entity.Product
import shiverawe.github.com.receipt.entity.Receipt
import shiverawe.github.com.receipt.entity.Shop
import shiverawe.github.com.receipt.ui.App
import java.util.ArrayList

class ReceiptRepository {
    private val receipt: Receipt? = null
    private var singleReceipt: Single<Receipt?>? = null
    private var parameters: Map<String, String>? = null
    fun getReceipt(options: Map<String, String>): Single<Receipt?> {
        parameters = options
        singleReceipt = App.api.getReceipt(options).map { it -> map(it)}
        return singleReceipt!!
    }

    fun saveReceipt(): Single<CreateResponce> {
        val createRequest = CreateRequest(parameters?.get("fn")?: "", parameters?.get("fp")?: "", parameters?.get("i")?: "", parameters?.get("s")?: "", parameters?.get("t")?: "")
        return App.api.createReceipt(createRequest)
    }
    private fun map(response: ReceiptResponce?): Receipt? {
        if (response?.meta == null || response.items == null) return null
        val products = ArrayList<Product>()
        response.items.forEach {
            products.add(Product(it.text ?: "", it.price
                    ?: 0.0, it.amount ?: 0.0))
        }
        val fn = response.meta.fn.toString()
        val fp = response.meta.fp.toString()
        val i = response.meta.fd.toString()
        val t = (response.meta.date!!.toLong() * 1000).toString()
        val meta = Meta( t, fn, i, fp, response.meta.sum?.toDouble()?: 0.0)
        val shop = Shop(response.meta.date.toLong() * 1000, response.meta.place ?: "", response.meta.sum ?: "")
        return Receipt(shop, meta, products)
    }


}