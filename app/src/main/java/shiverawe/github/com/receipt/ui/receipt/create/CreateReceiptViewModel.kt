package shiverawe.github.com.receipt.ui.receipt.create

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import shiverawe.github.com.receipt.domain.entity.BaseResult
import shiverawe.github.com.receipt.domain.entity.SingleEvent
import shiverawe.github.com.receipt.domain.entity.base.Meta
import shiverawe.github.com.receipt.domain.entity.base.ReceiptHeader
import shiverawe.github.com.receipt.domain.interactor.create_receipt.ICreateReceiptInteractor
import shiverawe.github.com.receipt.ui.receipt.create.state.CreateReceiptState
import shiverawe.github.com.receipt.ui.receipt.create.state.ErrorState

class CreateReceiptViewModel(private val interactor: ICreateReceiptInteractor) : ViewModel() {

    val state: MutableLiveData<CreateReceiptState> = MutableLiveData(CreateReceiptState())

    private var currentJob: Job? = null

    fun createReceipt(qrCodeData: String) {
        if (state.value?.isWaiting == true) return

        state.value = CreateReceiptState(isWaiting = true)

        currentJob?.cancel()
        currentJob = viewModelScope.launch {
            setResult(interactor.createReceipt(qrCodeData))
        }
    }

    fun createReceipt(meta: Meta) {
        if (state.value?.isWaiting == true) return
        state.value = CreateReceiptState(isWaiting = true)

        currentJob?.cancel()
        currentJob = viewModelScope.launch {
            setResult(interactor.createReceipt(meta))
        }
    }

    fun onCancelWaiting() {
        cancelTask()
        state.value = state.value?.apply { isWaiting = false }
    }

    private fun setResult(result: BaseResult<ReceiptHeader>) {
        if (result.isCancel) return

        result.result?.let { header ->
            state.value = CreateReceiptState(receiptHeader = header)
        }

        result.error?.let { receiptError ->
            state.value = CreateReceiptState(
                isWaiting = false,
                error = SingleEvent(ErrorState(receiptError.throwable, receiptError.type))
            )
        }
    }

    private fun cancelTask() {
        currentJob?.cancel()
    }
}