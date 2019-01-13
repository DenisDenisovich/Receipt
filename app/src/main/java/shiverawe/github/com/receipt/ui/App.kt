package shiverawe.github.com.receipt.ui

import android.app.Application
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import shiverawe.github.com.receipt.data.network.Api


class App: Application() {
    companion object {
        lateinit var api: Api
    }

    override fun onCreate() {
        super.onCreate()
        val retrofit = Retrofit.Builder()
                .baseUrl("http://receipt.shefer.space/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        api = retrofit.create(Api::class.java)
    }
}