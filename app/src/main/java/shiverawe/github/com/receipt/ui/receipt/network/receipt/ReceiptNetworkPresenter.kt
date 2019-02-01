package shiverawe.github.com.receipt.ui.receipt.network.receipt

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableSingleObserver
import shiverawe.github.com.receipt.data.network.entity.create.CreateResponce
import shiverawe.github.com.receipt.data.repository.ReceiptRepository
import shiverawe.github.com.receipt.entity.Receipt
import java.lang.Exception
import java.util.*

class ReceiptNetworkPresenter : ReceiptNetworkContract.Presenter {
    private val receiptRepository = ReceiptRepository()
    private var saveResponse: CreateResponce? = null
    private var view: ReceiptNetworkContract.View? = null
    private var getDisposable: Disposable? = null
    private var saveDisposable: Disposable? = null
    private val options = HashMap<String, String>()

    override fun attach(view: ReceiptNetworkContract.View) {
        this.view = view
    }

    override fun detach() {
        view = null
        getDisposable?.dispose()
        saveDisposable?.dispose()
    }

    override fun setQrData(qrData: String) {
        try {
            val parameters = qrData.split("&")
            parameters.forEach { parameter ->
                val key = parameter.substring(0, parameter.indexOf("="))
                val value = parameter.substring(parameter.indexOf("=") + 1, parameter.length)
                options[key] = value
            }
            getReceipt()
        } catch (e: Exception) {
            view?.error()
        }
    }

    override fun getReceipt() {
        view?.showProgress()
        getDisposable = receiptRepository.getReceipt(options).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<Receipt?>() {
                    override fun onSuccess(receipt: Receipt) {
                        view?.showReceipt(receipt)
                    }

                    override fun onError(e: Throwable) {
                        view?.showGetReceiptError()
                    }
                })
    }

    override fun save() {
        saveDisposable = receiptRepository.saveReceipt().observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<CreateResponce>() {
                    override fun onSuccess(responce: CreateResponce) {
                        saveResponse = responce
                        when {
                            responce.status == "OK" -> view?.receiptIsSaved()
                            responce.status == "Receipt already exist" -> view?.receiptIsAlreadyExist()
                            else -> view?.receiptIsNotSaved()
                        }
                    }

                    override fun onError(e: Throwable) {
                        view?.receiptIsNotSaved()
                    }
                })
    }
}