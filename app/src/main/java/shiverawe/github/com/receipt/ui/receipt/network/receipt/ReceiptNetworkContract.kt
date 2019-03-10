package shiverawe.github.com.receipt.ui.receipt.network.receipt

import shiverawe.github.com.receipt.entity.receipt.base.Receipt

interface ReceiptNetworkContract {
    interface View {
        fun showReceipt(receipt: Receipt)
        fun showGetReceiptError()
        fun error()
        fun showProgress()
        fun receiptIsSaved()
        fun receiptIsAlreadyExist()
        fun receiptIsNotSaved()
    }

    interface Presenter {
        fun attach(view: View)
        fun detach()
        fun setQrData(qrData: String)
        fun getReceipt()
        fun save()
    }
}