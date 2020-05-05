package shiverawe.github.com.receipt.domain.interactor.create_receipt

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import shiverawe.github.com.receipt.data.network.utils.isOnline
import shiverawe.github.com.receipt.domain.entity.dto.ErrorType
import shiverawe.github.com.receipt.domain.entity.dto.Meta
import shiverawe.github.com.receipt.domain.repository.IReceiptRepository
import shiverawe.github.com.receipt.utils.toLongWithSeconds
import java.lang.Exception

class CreateReceiptInteractor(private val repository: IReceiptRepository) : ICreateReceiptInteractor {

    private var disposable: Disposable? = null

    override fun createReceipt(qrRawData: String, resultListener: CreateReceiptListener) {
        try {
            val meta = parseQrCode(qrRawData)
            createReceiptNetwork(meta, resultListener)
        } catch (e: ParseQrException) {
            resultListener.onError(error = e, errorType = ErrorType.ERROR)
        }
    }

    override fun createReceipt(meta: Meta, resultListener: CreateReceiptListener) {
        createReceiptNetwork(meta, resultListener)
    }

    // Go to network for creation receipt
    private fun createReceiptNetwork(meta: Meta, resultListener: CreateReceiptListener) {
        if (!isOnline()) {
            resultListener.onError(errorType = ErrorType.OFFLINE)
            return
        }
        // post request for creation receipt
        disposable?.dispose()
        disposable = repository.saveReceipt(meta)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                resultListener.onSuccess(it, meta)
            }, {
                resultListener.onError(error = it, errorType = ErrorType.ERROR)
            })
    }

    override fun cancelWork() {
        disposable?.dispose()
    }

    /**
     * Parse raw data from qr code to [Meta]
     * Throw Exception if [qrRawData] does not match the pattern
     * Pattern:
     * t=20200504T180400&s=483.02&fn=9280440300531305&i=32196&fp=3906617997&n=1
     **/
    private fun parseQrCode(qrRawData: String): Meta {
        try {
            val meta = HashMap<String, String>()
            val parameters = qrRawData.split("&")
            parameters.forEach { parameter ->
                val key = parameter.substring(0, parameter.indexOf("="))
                val value = parameter.substring(parameter.indexOf("=") + 1, parameter.length)
                meta[key] = value
            }
            return Meta(
                t = (meta["t"] ?: "").toLongWithSeconds(),
                fn = meta["fn"] ?: "",
                fp = meta["fp"] ?: "",
                fd = meta["i"] ?: "",
                s = meta["s"]?.toDoubleOrNull() ?: 0.0
            )
        } catch (e: Exception) {
            throw ParseQrException(e)
        }
    }
}