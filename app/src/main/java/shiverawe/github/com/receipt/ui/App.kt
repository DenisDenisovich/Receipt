package shiverawe.github.com.receipt.ui

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import shiverawe.github.com.receipt.di.*

class App : Application() {
    companion object {
        lateinit var appContext: Context
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        initKoin()
    }

    fun initKoin() {
        stopKoin()
        startKoin {
            androidContext(this@App)
            modules(
                listOf(
                    monthModule,
                    receiptModule,
                    accountModule,
                    dbModule,
                    networkModule
                )
            )
        }
    }
}