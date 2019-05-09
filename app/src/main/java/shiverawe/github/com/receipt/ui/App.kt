package shiverawe.github.com.receipt.ui

import android.app.Application
import android.content.Context
import shiverawe.github.com.receipt.R
import java.lang.Exception
import com.yandex.metrica.YandexMetrica
import com.yandex.metrica.YandexMetricaConfig
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import shiverawe.github.com.receipt.di.*


class App : Application() {
    companion object {
        lateinit var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        initYandexMetric()
        initKoin()
    }

    private fun initYandexMetric() {
        try {
            val config = YandexMetricaConfig.newConfigBuilder(getString(R.string.yandex_key)).build()
            YandexMetrica.activate(applicationContext, config)
            YandexMetrica.enableActivityAutoTracking(this)
        } catch (e: Exception) {
        }
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@App)
            modules(monthModule,
                    receiptModule,
                    mappersModule,
                    utilsModule,
                    dbModule,
                    networkModule
            )
        }
    }
}