package shiverawe.github.com.receipt.data.network.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

fun createRetrofit(baseUrl: String): Api {
    val okHttpClient = OkHttpClient.Builder()
        .readTimeout(180, TimeUnit.SECONDS)
        .connectTimeout(180, TimeUnit.SECONDS)
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    return retrofit.create(Api::class.java)
}
