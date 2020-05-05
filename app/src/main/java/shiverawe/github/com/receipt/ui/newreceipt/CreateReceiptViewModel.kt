package shiverawe.github.com.receipt.ui.newreceipt

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import shiverawe.github.com.receipt.domain.entity.dto.Meta
import shiverawe.github.com.receipt.domain.interactor.create_receipt.CreateReceiptListener
import shiverawe.github.com.receipt.domain.entity.dto.ErrorType
import shiverawe.github.com.receipt.domain.interactor.create_receipt.ICreateReceiptInteractor

class CreateReceiptViewModel(private val interactor: ICreateReceiptInteractor) : ViewModel() {

    val state: MutableLiveData<CreateReceiptState> = MutableLiveData(QrCodeState())
    private val qrCodeState: QrCodeState?
        get() = state.value as? QrCodeState
    private val manualState: ManualState?
        get() = state.value as? ManualState

    // callback of receipt creation
    private val createReceiptListener = object : CreateReceiptListener {
        override fun onSuccess(id: Long, meta: Meta) {
            state.value = SuccessState(meta.t)
        }

        override fun onError(error: Throwable?, errorType: ErrorType) {
            if (qrCodeState != null) {
                // current state is QrCodeState. Set error to QrCodeState
                state.value = QrCodeState(isWaiting = false, error = ErrorState(error, type = errorType))
            } else if (manualState != null) {
                // current state is ManualState. Set error to ManualState
                state.value = ManualState(isWaiting = false, error = ErrorState(error, type = errorType))
            }
        }
    }

    fun goToManualScreen(isFirstScreen: Boolean = false) {
        state.value = ManualState(isFirstScreen = isFirstScreen)
    }

    fun goBack() {
        if (qrCodeState != null) {
            state.value = ExitState
        } else if (manualState != null) {
            if (manualState?.isFirstScreen == true) {
                state.value = ExitState
            } else {
                state.value = QrCodeState()
            }
        }
        cancelTask()
    }

    fun createReceipt(qrCodeData: String) {
        if (qrCodeState?.isWaiting == true) return
        state.value = QrCodeState(isWaiting = true)
        interactor.createReceipt(qrCodeData, createReceiptListener)
    }

    fun createReceipt(meta: Meta) {
        if (manualState?.isWaiting == true) return
        state.value = ManualState(isWaiting = true)
        interactor.createReceipt(meta, createReceiptListener)
    }

    fun showError(message: String? = null) {
        if (qrCodeState != null) {
            state.value = QrCodeState(error = ErrorState(message = message))
        } else if (manualState != null) {
            state.value = ManualState(error = ErrorState(message = message))
        }
    }

    fun onShowError() {
        qrCodeState?.error = null
        manualState?.error = null
    }

    fun onCancelWaiting() {
        cancelTask()
        qrCodeState?.let {
            state.value = it.apply { isWaiting = false }
        }
        manualState?.let {
            state.value = it.apply { isWaiting = false }
        }
    }

    private fun cancelTask() {
        interactor.cancelWork()
    }
}