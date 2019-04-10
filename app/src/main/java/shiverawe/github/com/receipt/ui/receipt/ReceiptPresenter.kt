package shiverawe.github.com.receipt.ui.receipt

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import shiverawe.github.com.receipt.data.repository.ReceiptRepository
import java.lang.Exception

class ReceiptPresenter {
    var repository = ReceiptRepository()
    var view: ReceiptView? = null
    var disposable: Disposable? = null
    fun attach(view: ReceiptView) {
        this.view = view
    }

    fun detach() {
        view = null
        disposable?.dispose()
    }

    fun getReceiptById(receiptId: Long) {
        view?.showProgress()
        disposable = repository.getReceiptById(receiptId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({receipt ->
                    view?.showReceipt(receipt)
                }, {
                    view?.showError(Throwable("receipt = null"))
                })
    }

    fun getReceiptByMeta(options: String) {
        view?.showProgress()
        parseOptions(options)?.let { meta ->
            disposable = repository.getReceipt(meta)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({receipt ->
                        if (receipt == null) view?.showError(Throwable("receipt = null"))
                        else view?.showReceipt(receipt)
                    }, {
                        view?.showError(it)
                    })
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