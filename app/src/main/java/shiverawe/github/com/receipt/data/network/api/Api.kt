package shiverawe.github.com.receipt.data.network.api

import retrofit2.Response
import retrofit2.http.*
import shiverawe.github.com.receipt.data.network.entity.account.LoginRequest
import shiverawe.github.com.receipt.data.network.entity.account.PasswordResetRequest
import shiverawe.github.com.receipt.data.network.entity.account.SignUpRequest
import shiverawe.github.com.receipt.data.network.entity.create.CreateRequest
import shiverawe.github.com.receipt.data.network.entity.item.ItemRequest
import shiverawe.github.com.receipt.data.network.entity.item.ItemResponse
import shiverawe.github.com.receipt.data.network.entity.receipt.ReceiptRequest
import shiverawe.github.com.receipt.data.network.entity.receipt.ReceiptResponse
import shiverawe.github.com.receipt.domain.entity.BaseResult

interface Api {

    @PUT("api/items")
    suspend fun getProducts(@Body request: ItemRequest): List<ItemResponse>

    @PUT("api/receipts")
    suspend fun getReceipts(@Body receiptRequest: ReceiptRequest): List<ReceiptResponse>

    @POST("api/create")
    suspend fun createReceipt(@Body createRequest: CreateRequest): ReceiptResponse

    @POST("api/login")
    suspend fun login(@Body loginRequest: LoginRequest): String

    @POST("api/passwordRestore")
    suspend fun reset(@Body passwordResetRequest: PasswordResetRequest): Response<Unit>

    @POST("api/signUp")
    suspend fun signUp(@Body signUpRequest: SignUpRequest): Response<Unit>
}