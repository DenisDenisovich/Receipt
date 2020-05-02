package shiverawe.github.com.receipt.ui.newreceipt

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import shiverawe.github.com.receipt.domain.entity.dto.Meta
import shiverawe.github.com.receipt.domain.repository.IReceiptRepository
import shiverawe.github.com.receipt.utils.toLongWithSeconds
import java.lang.Exception
import kotlin.collections.HashMap

class CreateReceiptViewModel(private val repository: IReceiptRepository) : ViewModel() {

    val state: MutableLiveData<CreateReceiptState> by lazy {
        MutableLiveData(QrCodeState())
    }
    private val qrCodeState: QrCodeState?
        get() = state.value as? QrCodeState
    private val manualState: ManualState?
        get() = state.value as? ManualState
    private val successState: SuccessState?
        get() = state.value as? SuccessState

    private var disposable: Disposable? = null

    fun qrCodeScreenIsActive() {
        state.value = QrCodeState()
    }

    fun manualScreenIsActive() {
        state.value = ManualState()
    }

    fun createReceipt(qrCodeData: String) {
        state.value = QrCodeState(isWaiting = true)
        val meta = parseQrCode(qrCodeData)
        if (meta == null) {
            state.value = QrCodeState(error = ErrorState())
            return
        }
        disposable?.dispose()
        disposable = repository.saveReceipt(meta)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                state.value = SuccessState(meta.t)
            }, {
                state.value = qrCodeState?.apply {
                    isWaiting = false
                    error = ErrorState(it)
                }
            })
    }

    fun createReceipt(meta: Meta) {
        state.value = ManualState(isWaiting = true)
        disposable?.dispose()
        disposable = repository.saveReceipt(meta)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                state.value = SuccessState(meta.t)
            }, {
                state.value = manualState?.apply {
                    isWaiting = false
                    error = ErrorState(it)
                }
            })
    }

    fun onShowMessage() {
        qrCodeState?.error = null
        manualState?.error = null
    }

    fun OnCancelWaiting() {
        qrCodeState?.let {
            state.value = it.apply { isWaiting = false }
        }
        manualState?.let {
            state.value = it.apply { isWaiting = false }
        }
    }

    private fun onClose() {
        disposable?.dispose()
    }

    private fun parseQrCode(data: String): Meta? {
        val meta = HashMap<String, String>()
        try {
            val parameters = data.split("&")
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
            return null
        }
    }

}