package shiverawe.github.com.receipt.ui.receipt.network

import shiverawe.github.com.receipt.entity.Receipt

interface ReceiptNetworkContract {
    interface View {
        fun showReceipt(receipt: Receipt)
        fun showError(message: String)
        fun showProgress()
        fun receiptIsSaved()
        fun receiptIsNotSaved()
    }

    interface Presenter {
        fun attach(view: View)
        fun detach()
        fun setQrData(qrData: String)
        fun getReceipt()
        fun save()
        fun destroy()
    }
}