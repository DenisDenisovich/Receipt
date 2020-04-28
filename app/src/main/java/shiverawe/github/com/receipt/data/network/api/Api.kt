package shiverawe.github.com.receipt.data.network.api

import io.reactivex.Single
import retrofit2.http.*
import shiverawe.github.com.receipt.data.network.entity.create.CreateRequest
import shiverawe.github.com.receipt.data.network.entity.create.CreateResponce
import shiverawe.github.com.receipt.data.network.entity.item.ItemRequest
import shiverawe.github.com.receipt.data.network.entity.item.ItemResponse
import shiverawe.github.com.receipt.data.network.entity.receipt.ReceiptRequest
import shiverawe.github.com.receipt.data.network.entity.receipt.ReceiptResponse

interface Api {

    @PUT("api/items")
    fun getProducts(@Body request: ItemRequest): Single<ArrayList<ItemResponse>>

    @PUT("api/receipts")
    fun getReceipts(@Body receiptRequest: ReceiptRequest): Single<ArrayList<ReceiptResponse>>

    @POST("rest/create")
    fun createReceipt(@Body reportRequest: CreateRequest): Single<CreateResponce>
}