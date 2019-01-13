package shiverawe.github.com.receipt.data.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import shiverawe.github.com.receipt.data.network.entity.get.ReceiptResponce
import shiverawe.github.com.receipt.data.network.entity.report.ReportRequest


interface Api {
    @GET("rest/get")
    fun getReceipt(@Query("fn") fn: Int, @Query("fd") fd: Int, @Query("fp") fp: Int, @Query("date") date: Int, @Query("sum") sum: Int): Call<List<ReceiptResponce>>
    @POST("rest/report")
    fun getReceiptForMonth(@Body reportRequest: ReportRequest): Call<ArrayList<ReceiptResponce>>
   /* @GET("rest/get")
    fun saveReceipt(@Query("fn") fn: Int, @Query("fd") fd: Int, @Query("fp") fp: Int, @Query("date") date: Int, @Query("sum") sum: Int): Call<List<ReceiptResponce>>*/
}