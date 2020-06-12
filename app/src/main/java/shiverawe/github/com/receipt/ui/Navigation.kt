package shiverawe.github.com.receipt.ui

interface Navigation {
    fun openLogin()
    fun openHistory()
    fun openSettings()
    fun openQr()
    fun openReceipt(receiptId: Long)
}