package shiverawe.github.com.receipt.ui.history.month

import shiverawe.github.com.receipt.domain.entity.base.ReceiptHeader

interface MonthContract {
    interface View {
        fun setReceipts(items: ArrayList<ReceiptHeader>)
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
    }
}