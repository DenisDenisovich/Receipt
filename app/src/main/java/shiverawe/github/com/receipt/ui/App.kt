package shiverawe.github.com.receipt.ui

import android.app.Application
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import shiverawe.github.com.receipt.data.network.Api
import java.util.concurrent.TimeUnit


class App: Application() {
    companion object {
        lateinit var api: Api
    }

    override fun onCreate() {
        super.onCreate()
        val okHttpClient = OkHttpClient.Builder()
                .readTimeout(20, TimeUnit.SECONDS)
                .connectTimeout(20, TimeUnit.SECONDS)
                .build()
        val retrofit = Retrofit.Builder()
                .baseUrl("http://receipt.shefer.space/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        api = retrofit.create(Api::class.java)
    }
}