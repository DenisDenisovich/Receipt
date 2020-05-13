package shiverawe.github.com.receipt.data.network.utils

import android.content.Context
import android.net.ConnectivityManager
import shiverawe.github.com.receipt.ui.App

fun isOffline(): Boolean {
    val connectionManager = App.appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return !(connectionManager.activeNetworkInfo?.isConnected ?: false)
}
