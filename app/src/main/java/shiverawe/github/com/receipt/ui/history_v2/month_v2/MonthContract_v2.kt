package shiverawe.github.com.receipt.ui.history_v2.month_v2

import shiverawe.github.com.receipt.entity.receipt.month.ReceiptMonth_v2

interface MonthContract_v2 {
    interface View {
        fun setReceipts(items: ArrayList<ReceiptMonth_v2>)
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