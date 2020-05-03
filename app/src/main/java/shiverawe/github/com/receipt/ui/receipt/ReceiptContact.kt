package shiverawe.github.com.receipt.ui.receipt

import shiverawe.github.com.receipt.domain.entity.dto.Receipt

interface ReceiptContact {
    interface View {
        fun showReceipt(receipt: Receipt)
        fun showError(error: Throwable)
    }

    interface Presenter {
        fun attach(view: View)
        fun detach()
        fun getReceiptById(receiptId: Long)
        fun getReceiptByMeta(options: String)
    }

}