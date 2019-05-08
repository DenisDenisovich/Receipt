package shiverawe.github.com.receipt.data.network

import io.reactivex.Single
import shiverawe.github.com.receipt.data.network.entity.create.CreateRequest
import shiverawe.github.com.receipt.data.network.entity.create.CreateResponce
import shiverawe.github.com.receipt.data.network.mapper.MapperNetwork
import shiverawe.github.com.receipt.domain.entity.receipt.base.Receipt
import shiverawe.github.com.receipt.ui.App

class ReceiptNetwork: IReceiptNetwork {

    private val mapper = MapperNetwork()
    private var parameters: Map<String, String>? = null
    override fun getReceipt(options: Map<String, String>): Single<Receipt?> {
        parameters = options
        return App.api.getReceipt(options).map { response -> mapper.getToReceipt(response) }
    }

    override fun saveReceipt(): Single<CreateResponce> {
        val fn = parameters?.get("fn") ?: ""
        val fp = parameters?.get("fn") ?: ""
        val i = parameters?.get("fn") ?: ""
        val s = parameters?.get("fn") ?: ""
        val t = parameters?.get("fn") ?: ""
        val createRequest = CreateRequest(fn, fp, i, s, t)
        return App.api.createReceipt(createRequest)
    }
}