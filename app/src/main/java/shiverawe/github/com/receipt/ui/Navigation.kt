package shiverawe.github.com.receipt.ui

import shiverawe.github.com.receipt.entity.Receipt

interface Navigation {
    fun openNavigationDrawable()
    fun closeNavigationDrawable()
    fun openHistory()
    fun openQr()
    fun openReceipt(receiptId: Long)
}