package shiverawe.github.com.receipt.ui.receipt

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import shiverawe.github.com.receipt.domain.entity.dto.Meta
import shiverawe.github.com.receipt.domain.repository.IReceiptRepository
import shiverawe.github.com.receipt.utils.Metric
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class ReceiptPresenter(private val repository: IReceiptRepository): ReceiptContact.Presenter {
    private var view: ReceiptContact.View? = null
    private var disposable: Disposable? = null
    private val dateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())

    override fun attach(view: ReceiptContact.View) {
        this.view = view
    }

    override fun detach() {
        view = null
        disposable?.dispose()
    }

    override fun getReceiptById(receiptId: Long) {
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
        parseOptions(options)?.let { meta ->
            disposable = repository.getReceipt(meta)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ receipt ->
                        totalTime = ((System.currentTimeMillis() - startTime) / 1000).toInt()
                        if (receipt == null) {
                            val t = Throwable("receipt = null")
                            Metric.sendNewReceiptError(options, totalTime, t)
                            view?.showError(t)
                        } else {
                            view?.showReceipt(receipt)
                            Metric.sendSuccessNewReceipt(options, totalTime)

                        }
                    }, {
                        totalTime = ((System.currentTimeMillis() - startTime) / 1000).toInt()
                        Metric.sendNewReceiptError(options, totalTime, it)
                        view?.showError(it)
                    })
        }
    }

    private fun parseOptions(options: String): Meta? {
        val meta = HashMap<String, String>()
        try {
            val parameters = options.split("&")
            parameters.forEach { parameter ->
                val key = parameter.substring(0, parameter.indexOf("="))
                val value = parameter.substring(parameter.indexOf("=") + 1, parameter.length)
                meta[key] = value
            }
            return Meta(
                t = dateFormatter.parse(meta["t"] ?: "")?.time ?: 0L,
                fn = meta["fn"] ?: "",
                fp = meta["fp"] ?: "",
                fd = meta["i"] ?: "",
                s = meta["s"]?.toDoubleOrNull() ?: 0.0
            )
        } catch (e: Exception) {
            view?.showError(Throwable("receipt = null"))
            return null
        }
    }
}