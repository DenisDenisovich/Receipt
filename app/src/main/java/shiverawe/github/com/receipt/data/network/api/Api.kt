package shiverawe.github.com.receipt.data.network.api

import io.reactivex.Single
import retrofit2.http.*
import shiverawe.github.com.receipt.data.network.entity.create.CreateRequest
import shiverawe.github.com.receipt.data.network.entity.create.CreateResponse
import shiverawe.github.com.receipt.data.network.entity.item.ItemRequest
import shiverawe.github.com.receipt.data.network.entity.item.ItemResponse
import shiverawe.github.com.receipt.data.network.entity.receipt.ReceiptRequest
import shiverawe.github.com.receipt.data.network.entity.receipt.ReceiptResponse

interface Api {

    @PUT("api/items")
    fun getProducts(@Body request: ItemRequest): Single<List<ItemResponse>>

    @PUT("api/receipts")
    fun getReceipts(@Body receiptRequest: ReceiptRequest): Single<List<ReceiptResponse>>

    @POST("api/create")
    fun createReceipt(@Body reportRequest: CreateRequest): Single<CreateResponse>
}