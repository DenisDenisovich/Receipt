package shiverawe.github.com.receipt.ui.history.month

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import shiverawe.github.com.receipt.data.network.entity.report.ReportRequest
import shiverawe.github.com.receipt.data.repository.MonthRepository
import shiverawe.github.com.receipt.entity.receipt.month.ReceiptMonth
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*
import kotlin.collections.ArrayList

class MonthPresenter(dateFrom: Int) : MonthContract.Presenter {
    private val repository = MonthRepository()
    private var reportRequest: ReportRequest
    var receiptDisposable: Disposable? = null
    var view: MonthContract.View? = null
    private var receipts: ArrayList<ReceiptMonth?>? = null
    private var isError = false

    init {
        val date = Date()
        val calendar = GregorianCalendar()
        date.time = dateFrom.toLong() * 1000
        calendar.time = date
        calendar.add(Calendar.MONTH, 1)
        val dateTo = (calendar.timeInMillis / 1000).toInt()
        reportRequest = ReportRequest(dateFrom, dateTo)
    }

    override fun attach(view: MonthContract.View) {
        this.view = view
    }

    override fun update() {
        receipts = null
        isError = false
        receiptDisposable?.dispose()
        getReceiptsData()
    }

    override fun getReceiptsData() {
        if (receipts != null) {
            setReceiptsData()
            return
        }
        receiptDisposable?.dispose()
        receiptDisposable = repository.getMonthReceipt(reportRequest)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    receipts = response
                    setReceiptsData()
                },{
                    isError = true
                    view?.showError()
                })
    }

    private fun setReceiptsData() {
        if (isError) {
            view?.showError()
        } else {
            if (receipts!!.size == 0) {
                view?.showEmptyDataMessage()
            } else {
                view?.setReceipts(receipts!!)
                //view?.setTotalSum(totalSum)
            }
        }
    }

    override fun detach() {
        view = null
        receiptDisposable?.dispose()
    }
}