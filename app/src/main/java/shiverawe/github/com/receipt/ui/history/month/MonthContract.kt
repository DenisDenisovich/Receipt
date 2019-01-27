package shiverawe.github.com.receipt.ui.history.month

import shiverawe.github.com.receipt.entity.Receipt

interface MonthContract {
    interface View {
        fun setReceipts(receipts: ArrayList<Receipt?>)
        fun setTotalSum(totalSum: String)
        fun showProgressbar()
        fun showError()
        fun showEmptyDataMessage()
    }

    interface Presenter {
        fun attach(view: View)
        fun detach()
        fun getReceiptsData()
        fun update()
        fun destroy()
    }
}