package shiverawe.github.com.receipt.ui.receipt

import shiverawe.github.com.receipt.domain.entity.receipt.base.Receipt

interface ReceiptView {
    fun showReceipt(receipt: Receipt)
    fun showError(error: Throwable)
    fun showProgress()
}