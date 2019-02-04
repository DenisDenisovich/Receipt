package shiverawe.github.com.receipt.ui.history.month

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableSingleObserver
import shiverawe.github.com.receipt.data.network.entity.report.ReportRequest
import shiverawe.github.com.receipt.data.repository.MonthRepository
import shiverawe.github.com.receipt.entity.Receipt
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*
import kotlin.collections.ArrayList

class MonthPresenter(dateFrom: Int) : MonthContract.Presenter {
    private val repository = MonthRepository()
    private var reportRequest: ReportRequest
    var receiptDisposable: Disposable? = null
    var view: MonthContract.View? = null
    private var receipts: ArrayList<Receipt?>? = null
    private var totalSum = ""
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
        totalSum = ""
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
                .subscribeWith(object : DisposableSingleObserver<ArrayList<Receipt?>>() {
                    override fun onSuccess(t: ArrayList<Receipt?>) {
                        receipts = t
                        var sum = 0.0
                        receipts!!.forEach { receipt ->
                            sum += receipt?.meta?.s?:0.0
                        }
                        totalSum = BigDecimal(sum).setScale(2, RoundingMode.DOWN).toString() + " р"
                        setReceiptsData()
                    }
                    override fun onError(e: Throwable) {
                        isError = true
                        view?.showError()
                    }
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
                view?.setTotalSum(totalSum)
            }
        }
    }

    override fun detach() {
        view = null
        receiptDisposable?.dispose()
    }
}