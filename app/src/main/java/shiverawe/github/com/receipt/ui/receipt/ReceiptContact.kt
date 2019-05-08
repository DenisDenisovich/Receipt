package shiverawe.github.com.receipt.ui.receipt

import shiverawe.github.com.receipt.domain.entity.receipt.base.Receipt

interface ReceiptContact {
    interface ReceiptView {
        fun showReceipt(receipt: Receipt)
        fun showError(error: Throwable)
        fun showProgress()
    }

    interface ReceiptPresenter {
        fun detach()
        fun getReceiptById(receiptId: Long)
        fun getReceiptByMeta(options: String)
    }

}