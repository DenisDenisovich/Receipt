package shiverawe.github.com.receipt.ui.history.month

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import shiverawe.github.com.receipt.domain.entity.base.ReceiptHeader
import shiverawe.github.com.receipt.domain.repository.IMonthRepository
import shiverawe.github.com.receipt.utils.Metric
import shiverawe.github.com.receipt.utils.floorTwo
import java.util.*

class MonthPresenter(
        private val repository: IMonthRepository,
        private val dateFrom: Long
) : MonthContract.Presenter {

    private var receiptDisposable: Disposable? = null
    private var view: MonthContract.View? = null
    private val dateTo: Long
    private var receipts: ArrayList<ReceiptHeader> = ArrayList()
    private var totalSum: Double = 0.0
    private var isError = false

    init {
        val date = Date()
        val calendar = GregorianCalendar()
        date.time = dateFrom
        calendar.time = date
        calendar.add(Calendar.MONTH, 1)
        dateTo = calendar.timeInMillis
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
        receiptDisposable = repository.getMonthReceipt(dateFrom, dateTo)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    receipts = ArrayList()
                    totalSum = 0.0
                    response.forEach {
                        totalSum += it.meta.s
                        receipts.add(ReceiptHeader(it.receiptId, it.shop, it.meta))
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
                val sumStr = totalSum.floorTwo()
                view?.setTotalSum(sumStr)
            }
        }
    }

    override fun detach() {
        view = null
        receiptDisposable?.dispose()
    }
}