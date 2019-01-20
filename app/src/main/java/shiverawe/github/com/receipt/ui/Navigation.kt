package shiverawe.github.com.receipt.ui

import shiverawe.github.com.receipt.data.Receipt

interface Navigation {
    fun openNavigationDrawable()
    fun closeNavigationDrawable()
    fun openHistory()
    fun openQr()
    fun openReceipt(receipt: Receipt)
    fun openNetworkReceipt(receipt: Receipt, saveMeta: String)
}