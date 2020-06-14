package shiverawe.github.com.receipt.data.network.api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import shiverawe.github.com.receipt.utils.Storage
import java.util.concurrent.TimeUnit

class RetrofitBuilder(baseUrl: String, storage: Storage) {

    private val okHttpClient = OkHttpClient.Builder()
        .readTimeout(180, TimeUnit.SECONDS)
        .connectTimeout(180, TimeUnit.SECONDS)
        .addInterceptor {
            val request = it.request()
                .newBuilder()
                .header("Authorization", "Bearer ${storage.token}")
                .build()

            return@addInterceptor it.proceed(request)
        }
        .build()

    private val gson = GsonBuilder().setLenient().create()

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    val api: Api = retrofit.create(Api::class.java)
}