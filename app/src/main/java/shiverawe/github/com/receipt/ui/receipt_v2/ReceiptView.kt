package shiverawe.github.com.receipt.ui.receipt_v2

import shiverawe.github.com.receipt.entity.receipt.base.Receipt

interface ReceiptView {
    fun showReceipt(receipt: Receipt)
    fun showError(message: String)
    fun showProgress()
}