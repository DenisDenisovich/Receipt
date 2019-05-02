package shiverawe.github.com.receipt.utils

import com.yandex.metrica.YandexMetrica

object Metric {
    private const val NEW_RECEIPT_ERROR = "newReceiptError"
    private const val HISTORY_ERROR = "historyError"
    private const val NEW_RECEIPT_SUCCESS = "newReceiptSuccess"

    fun sendNewReceiptError(meta: String, time: Int, t: Throwable) {
        YandexMetrica.reportError(NEW_RECEIPT_ERROR, Throwable("time: $time seconds,\n$meta", t))
    }

    fun sendHistoryError(t: Throwable) {
        YandexMetrica.reportError(HISTORY_ERROR, t)
    }

    fun sendSuccessNewReceipt(meta: String, time: Int) {
        val message = "{\"$time seconds\":\"$meta\"}"
        YandexMetrica.reportEvent(NEW_RECEIPT_SUCCESS, message)
    }

}