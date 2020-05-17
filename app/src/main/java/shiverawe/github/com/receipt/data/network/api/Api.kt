package shiverawe.github.com.receipt.data.network.api

import retrofit2.http.*
import shiverawe.github.com.receipt.data.network.entity.create.CreateRequest
import shiverawe.github.com.receipt.data.network.entity.item.ItemRequest
import shiverawe.github.com.receipt.data.network.entity.item.ItemResponse
import shiverawe.github.com.receipt.data.network.entity.receipt.ReceiptRequest
import shiverawe.github.com.receipt.data.network.entity.receipt.ReceiptResponse

interface Api {

    @PUT("api/items")
    suspend fun getProducts(@Body request: ItemRequest): List<ItemResponse>

    @PUT("api/receipts")
    suspend fun getReceipts(@Body receiptRequest: ReceiptRequest): List<ReceiptResponse>

    @POST("api/create")
    suspend fun createReceipt(@Body createRequest: CreateRequest): ReceiptResponse
}