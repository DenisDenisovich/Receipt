package shiverawe.github.com.receipt.ui.receipt

import shiverawe.github.com.receipt.domain.entity.dto.base.Receipt

interface ReceiptContact {
    interface View {
        fun showReceipt(receipt: Receipt)
        fun showError(error: Throwable)
        fun showProgress()
    }

    interface Presenter {
        fun attach(view: ReceiptContact.View)
        fun detach()
        fun getReceiptById(receiptId: Long)
        fun getReceiptByMeta(options: String)
    }

}