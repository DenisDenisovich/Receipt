package shiverawe.github.com.receipt.data.network.api

import io.reactivex.Single
import retrofit2.http.*
import shiverawe.github.com.receipt.data.network.entity.create.CreateRequest
import shiverawe.github.com.receipt.data.network.entity.create.CreateResponce
import shiverawe.github.com.receipt.data.network.entity.item.ItemResponse
import shiverawe.github.com.receipt.data.network.entity.receipt.ReceiptRequest
import shiverawe.github.com.receipt.data.network.entity.receipt.ReceiptResponse
import shiverawe.github.com.receipt.data.network.entity.receipt.Report


interface Api {
    @GET("rest/get")
    fun getReceipt(@QueryMap options: Map<String, String>): Single<ItemResponse>
    @PUT("api/receipts")
    fun getReceiptForMonth(@Body receiptRequest: ReceiptRequest): Single<ArrayList<ReceiptResponse>>
    @POST("rest/create")
    fun createReceipt(@Body reportRequest: CreateRequest): Single<CreateResponce>
}