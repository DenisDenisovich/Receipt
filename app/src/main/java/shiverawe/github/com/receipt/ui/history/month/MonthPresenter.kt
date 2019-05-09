package shiverawe.github.com.receipt.ui.history.month

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import shiverawe.github.com.receipt.data.network.entity.report.ReportRequest
import shiverawe.github.com.receipt.domain.entity.dto.month.ReceiptMonth
import shiverawe.github.com.receipt.domain.repository.IMonthRepository
import shiverawe.github.com.receipt.utils.Metric
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*
import kotlin.collections.ArrayList

class MonthPresenter(
        private val repository: IMonthRepository,
        dateFrom: Int
) : MonthContract.Presenter {
    private var reportRequest: ReportRequest
    var receiptDisposable: Disposable? = null
    var view: MonthContract.View? = null
    private var receipts: ArrayList<ReceiptMonth> = ArrayList()
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

    override fun attach(view: MonthContract.View) {
        this.view = view
    }

    override fun update() {
        receipts.clear()
        isError = false
        receiptDisposable?.dispose()
        getReceiptsData()
    }

    override fun getReceiptsData() {
        val startTime = System.currentTimeMillis()
        var totalTime: Int
        receiptDisposable?.dispose()
        receiptDisposable = repository.getMonthReceipt(reportRequest)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    receipts = ArrayList()
                    totalSum = 0.0
                    response.forEach {
                        totalSum += it.meta.s
                        receipts.add(ReceiptMonth(it.receiptId, it.shop, it.meta))
                    }
                    setReceiptsData()
                }, {
                    isError = true
                    view?.showError()
                    totalTime = ((System.currentTimeMillis() - startTime) / 1000).toInt()
                    Metric.sendHistoryError(totalTime, it)
                })
    }

    private fun setReceiptsData() {
        if (isError) {
            view?.showError()
        } else {
            if (receipts.size == 0) {
                view?.setTotalSum("0")
                view?.showEmptyDataMessage()
            } else {

                view?.setReceipts(receipts)
                val sumStr = BigDecimal(totalSum).setScale(2, RoundingMode.DOWN).toString()
                view?.setTotalSum(sumStr)
            }
        }
    }

    override fun detach() {
        view = null
        receiptDisposable?.dispose()
    }
}