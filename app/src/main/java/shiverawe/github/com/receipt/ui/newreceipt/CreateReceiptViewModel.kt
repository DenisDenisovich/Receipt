package shiverawe.github.com.receipt.ui.newreceipt

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import shiverawe.github.com.receipt.domain.interactor.create_receipt.CreateReceiptErrorResult
import shiverawe.github.com.receipt.domain.interactor.create_receipt.CreateReceiptIsExistResult
import shiverawe.github.com.receipt.domain.interactor.create_receipt.CreateReceiptSuccessResult
import shiverawe.github.com.receipt.domain.entity.base.Meta
import shiverawe.github.com.receipt.domain.interactor.create_receipt.ICreateReceiptInteractor

class CreateReceiptViewModel(private val interactor: ICreateReceiptInteractor) : ViewModel() {

    val state: MutableLiveData<CreateReceiptUiState> = MutableLiveData(QrCodeState())
    private val qrCodeState: QrCodeState?
        get() = state.value as? QrCodeState
    private val manualState: ManualState?
        get() = state.value as? ManualState

    private var currentJob: Job? = null

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

        currentJob?.cancel()
        currentJob = viewModelScope.launch {
            when(val result = interactor.createReceipt(qrCodeData)) {
                is CreateReceiptSuccessResult -> {
                    state.value = SuccessState(result.meta.t)
                }

                is CreateReceiptIsExistResult -> {
                    state.value = ShowReceiptState(result.receiptHeader)
                }

                is CreateReceiptErrorResult -> {
                    qrCodeState?.let {
                        state.value = QrCodeState(
                            isWaiting = false,
                            error = ErrorState(result.error, type = result.type)
                        )
                    }
                }
            }
        }
    }

    fun createReceipt(meta: Meta) {
        if (manualState?.isWaiting == true) return
        state.value = ManualState(isWaiting = true)
        currentJob?.cancel()
        currentJob = viewModelScope.launch {
            when(val result = interactor.createReceipt(meta)) {
                is CreateReceiptSuccessResult -> {
                    state.value = SuccessState(result.meta.t)
                }

                is CreateReceiptIsExistResult -> {
                    state.value = ShowReceiptState(result.receiptHeader)
                }

                is CreateReceiptErrorResult -> {
                    manualState?.let {
                        state.value = ManualState(
                            isWaiting = false,
                            error = ErrorState(result.error, type = result.type)
                        )
                    }
                }
            }
        }
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
        currentJob?.cancel()
    }
}