package shiverawe.github.com.receipt.ui.history_v2.month_v2

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import shiverawe.github.com.receipt.R
import shiverawe.github.com.receipt.data.network.entity.report.ReportRequest
import shiverawe.github.com.receipt.data.repository.MonthRepository
import shiverawe.github.com.receipt.entity.receipt.month.ReceiptMonth_v2
import shiverawe.github.com.receipt.ui.App
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*
import kotlin.collections.ArrayList

class MonthPresenter_v2(dateFrom: Int) : MonthContract_v2.Presenter {
    private val repository = MonthRepository()
    private var reportRequest: ReportRequest
    var receiptDisposable: Disposable? = null
    var view: MonthContract_v2.View? = null
    private var receipts: ArrayList<ReceiptMonth_v2> = ArrayList()
    private var totalSum: Double = 0.0
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

    override fun attach(view: MonthContract_v2.View) {
        this.view = view
    }

    override fun update() {
        receipts.clear()
        isError = false
        receiptDisposable?.dispose()
        getReceiptsData()
    }

    override fun getReceiptsData() {
        receiptDisposable?.dispose()
        receiptDisposable = repository.getMonthReceipt(reportRequest)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    receipts = ArrayList()
                    totalSum = 0.0
                    response.forEach {
                        if (it != null) {
                            totalSum += it.meta.s
                            receipts.add(ReceiptMonth_v2(it.receiptId, it.shop, it.meta))
                        }
                    }
                    setReceiptsData()
                }, {
                    isError = true
                    view?.showError()
                })
    }

    private fun setReceiptsData() {
        if (isError) {
            view?.showError()
        } else {
            if (receipts.size == 0) {
                view?.setTotalSum("0 ${App.appContext.resources.getString(R.string.rubleSymbolJava)}")
                view?.showEmptyDataMessage()
            } else {

                view?.setReceipts(receipts)
                val sumStr = BigDecimal(totalSum).setScale(2, RoundingMode.DOWN).toString() + " " + App.appContext.resources.getString(R.string.rubleSymbolJava)
                view?.setTotalSum(sumStr)
            }
        }
    }

    override fun detach() {
        view = null
        receiptDisposable?.dispose()
    }
}