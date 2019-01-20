package shiverawe.github.com.receipt.data.network

import retrofit2.Call
import retrofit2.http.*
import shiverawe.github.com.receipt.data.network.entity.create.CreateRequest
import shiverawe.github.com.receipt.data.network.entity.create.CreateResponce
import shiverawe.github.com.receipt.data.network.entity.get.ReceiptResponce
import shiverawe.github.com.receipt.data.network.entity.report.Report
import shiverawe.github.com.receipt.data.network.entity.report.ReportRequest


interface Api {
    @GET("rest/get")
    fun getReceipt(@QueryMap options: Map<String, String>): Call<ReceiptResponce>
    @PUT("rest/report")
    fun getReceiptForMonth(@Body reportRequest: ReportRequest): Call<ArrayList<Report>>
    @GET("rest/get")
    fun createReceipt(@Body reportRequest: CreateRequest): Call<CreateResponce>
}