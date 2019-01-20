package shiverawe.github.com.receipt.data.network

import retrofit2.Call
import retrofit2.http.*
import shiverawe.github.com.receipt.data.network.entity.get.ReceiptResponce
import shiverawe.github.com.receipt.data.network.entity.report.Report
import shiverawe.github.com.receipt.data.network.entity.report.ReportRequest


interface Api {
    @GET("rest/get")
    fun getReceipt(@Query("fn") fn: Long, @Query("i") i: Long, @Query("fp") fp: Long, @Query("t") t: Long, @Query("s") s: Long): Call<ReceiptResponce>
    @PUT("rest/report")
    fun getReceiptForMonth(@Body reportRequest: ReportRequest): Call<ArrayList<Report>>
   /* @GET("rest/get")
    fun saveReceipt(@Query("fn") fn: Int, @Query("fd") fd: Int, @Query("fp") fp: Int, @Query("date") date: Int, @Query("sum") sum: Int): Call<List<ReceiptResponce>>*/
}