package shiverawe.github.com.receipt.ui.receipt

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import shiverawe.github.com.receipt.domain.repository.IReceiptRepository
import shiverawe.github.com.receipt.utils.Metric
import java.lang.Exception

class ReceiptPresenter(private val repository: IReceiptRepository) : ReceiptContact.Presenter {
    var view: ReceiptContact.View? = null
    var disposable: Disposable? = null
    override fun attach(view: ReceiptContact.View) {
        this.view = view
    }

    override fun detach() {
        view = null
        disposable?.dispose()
    }

    override fun getReceiptById(receiptId: Long) {
        view?.showProgress()
        disposable = repository.getReceiptById(receiptId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ receipt ->
                    view?.showReceipt(receipt)
                }, {
                    view?.showError(Throwable("receipt = null"))
                })
    }

    override fun getReceiptByMeta(options: String) {
        val startTime = System.currentTimeMillis()
        var totalTime: Int
        view?.showProgress()

        parseOptions(options)
                ?.let { meta ->
                    disposable = repository.getReceipt(meta)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    { receipt ->
                                        totalTime = ((System.currentTimeMillis() - startTime) / 1000).toInt()
                                        if (receipt == null) {
                                            val t = Throwable("receipt = null")
                                            Metric.sendNewReceiptError(options, totalTime, t)
                                            view?.showError(t)
                                        } else {
                                            view?.showReceipt(receipt)
                                            Metric.sendSuccessNewReceipt(options, totalTime)

                                        }
                                    },
                                    { throwable ->
                                        totalTime = ((System.currentTimeMillis() - startTime) / 1000).toInt()
                                        Metric.sendNewReceiptError(options, totalTime, throwable)
                                        view?.showError(throwable)
                                    }
                            )
                }
    }

    private fun parseOptions(options: String): Map<String, String>? {
        val meta = HashMap<String, String>()
        try {
            val parameters = options.split("&")
            parameters.forEach { parameter ->
                val key = parameter.substring(0, parameter.indexOf("="))
                val value = parameter.substring(parameter.indexOf("=") + 1, parameter.length)
                meta[key] = value
            }

            return meta
        } catch (e: Exception) {
            view?.showError(Throwable("receipt = null"))

            return null
        }
    }
}
