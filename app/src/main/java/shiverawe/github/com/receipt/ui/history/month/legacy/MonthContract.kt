package shiverawe.github.com.receipt.ui.history.month.legacy

import shiverawe.github.com.receipt.domain.entity.ErrorType
import shiverawe.github.com.receipt.domain.entity.base.ReceiptHeader

interface MonthContract {
    interface View {
        fun setReceipts(items: ArrayList<ReceiptHeader>)
        fun setTotalSum(totalSum: String)
        fun showProgressbar()
        fun showError(errorType: ErrorType)
        fun showErrorToast(errorType: ErrorType)
        fun showEmptyDataMessage()
    }

    interface Presenter {
        fun attach(view: View)
        fun detach()
        fun getReceiptsData(isRefresh: Boolean = false)
        fun update()
    }
}