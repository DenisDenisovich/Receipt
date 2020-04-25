package shiverawe.github.com.receipt.data.network.api

import io.reactivex.Single
import retrofit2.http.*
import shiverawe.github.com.receipt.data.network.entity.create.CreateRequest
import shiverawe.github.com.receipt.data.network.entity.create.CreateResponce
import shiverawe.github.com.receipt.data.network.entity.item.ReceiptResponse
import shiverawe.github.com.receipt.data.network.entity.receipt.Report
import shiverawe.github.com.receipt.data.network.entity.receipt.ReportRequest


interface Api {
    @GET("rest/get")
    fun getReceipt(@QueryMap options: Map<String, String>): Single<ReceiptResponse>
    @PUT("rest/report")
    fun getReceiptForMonth(@Body reportRequest: ReportRequest): Single<ArrayList<Report>>
    @POST("rest/create")
    fun createReceipt(@Body reportRequest: CreateRequest): Single<CreateResponce>
}