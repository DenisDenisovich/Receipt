package shiverawe.github.com.receipt.ui.history.month

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import shiverawe.github.com.receipt.domain.entity.ErrorType
import shiverawe.github.com.receipt.domain.entity.ReceiptResult
import shiverawe.github.com.receipt.domain.entity.base.ReceiptHeader
import shiverawe.github.com.receipt.domain.interactor.month.IMonthInteractor
import shiverawe.github.com.receipt.utils.floorTwo

class MonthViewModel(private val interactor: IMonthInteractor) : ViewModel() {

    val state: MutableLiveData<MonthUiState> = MutableLiveData()
    private var currentWork: Job? = null

    fun loadReceipts(date: Long, isRefresh: Boolean = false) {
        state.value = MonthUiState(inProgress = true)
        currentWork?.cancel()
        // load receipts
        currentWork = viewModelScope.launch {
            val receiptResult = interactor.getMonthReceipt(date)

            if (receiptResult.isCancel) return@launch

            val receipts = receiptResult.result ?: arrayListOf()
            val errorType = receiptResult.error?.type
            val sum = receipts.asSequence().map { it.meta.s }.sum().floorTwo()
            val message = getMessage(receipts, errorType, isRefresh)
            state.value = MonthUiState(receipts = receipts, sum = sum, message = message)
        }
    }

    private fun getMessage(receipts: List<ReceiptHeader>?, errorType: ErrorType?, isRefresh: Boolean): MessageType? {
        if (errorType == null) {
            return if (receipts.isNullOrEmpty()) MessageType.EMPTY_LIST
            else null
        }

        return when {
            // Hide Offline error if it's first load and receipts list is not empty
            errorType == ErrorType.OFFLINE && !isRefresh &&
                receipts?.isNotEmpty() == true -> null

            errorType == ErrorType.OFFLINE -> MessageType.OFFLINE

            else -> MessageType.ERROR
        }
    }
}