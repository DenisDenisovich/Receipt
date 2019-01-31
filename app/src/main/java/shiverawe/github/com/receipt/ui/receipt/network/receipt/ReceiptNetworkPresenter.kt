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
    private var receipt: Receipt? = null
    private var saveResponse: CreateResponce? = null
    private var showReceiptAfterAttach = false
    private var showSaveAfterAttach = false
    private var view: ReceiptNetworkContract.View? = null
    private var getDisposable: Disposable? = null
    private var saveDisposable: Disposable? = null
    private val options = HashMap<String, String>()

    override fun attach(view: ReceiptNetworkContract.View) {
        this.view = view
        if (showReceiptAfterAttach) {
            showReceiptAfterAttach = false
            if (receipt != null) view.showReceipt(receipt!!)
            else view.showGetReceiptError()
        }
        if (showSaveAfterAttach) {
            showSaveAfterAttach = false
            when {
                saveResponse?.status == "OK" -> view.receiptIsSaved()
                saveResponse?.status == "Receipt already exist" -> view.receiptIsAlreadyExist()
                else -> view.receiptIsNotSaved()
            }
        }

    }

    override fun detach() {
        view = null
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
                        showReceiptAfterAttach = view == null
                        this@ReceiptNetworkPresenter.receipt = receipt
                        view?.showReceipt(receipt)
                    }

                    override fun onError(e: Throwable) {
                        showReceiptAfterAttach = view == null
                        view?.showGetReceiptError()
                    }
                })
    }

    override fun save() {
        saveDisposable = receiptRepository.saveReceipt().observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<CreateResponce>() {
                    override fun onSuccess(responce: CreateResponce) {
                        saveResponse = responce
                        showSaveAfterAttach = view == null
                        when {
                            responce.status == "OK" -> view?.receiptIsSaved()
                            responce.status == "Receipt already exist" -> view?.receiptIsAlreadyExist()
                            else -> view?.receiptIsNotSaved()
                        }
                    }

                    override fun onError(e: Throwable) {
                        showSaveAfterAttach = view == null
                        view?.receiptIsNotSaved()
                    }
                })
    }

    override fun destroy() {
        getDisposable?.dispose()
        saveDisposable?.dispose()
    }
}