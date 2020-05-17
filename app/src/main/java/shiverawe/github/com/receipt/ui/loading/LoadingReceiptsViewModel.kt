package shiverawe.github.com.receipt.ui.loading

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import shiverawe.github.com.receipt.domain.entity.base.Meta
import shiverawe.github.com.receipt.domain.entity.base.ReceiptHeader
import shiverawe.github.com.receipt.domain.entity.base.ReceiptStatus
import shiverawe.github.com.receipt.domain.entity.base.Shop

class LoadingReceiptsViewModel : ViewModel() {

    private val state: MutableLiveData<LoadingReceiptUiState> = MutableLiveData()
    private var currentWork: Job? = null

    fun loadedReceipts() {
        state.value = LoadingReceiptUiState(inProgress = true)
        currentWork?.cancel()
        currentWork = viewModelScope.launch {
            delay(1000)
            state.value = LoadingReceiptUiState(getReceiptData())
        }
    }

    fun getState(): LiveData<LoadingReceiptUiState> = state

    private fun getReceiptData() = arrayListOf(
        ReceiptHeader(0, ReceiptStatus.FAILED, Shop(23432520, "", "", "2435.54"), Meta(23432520, "", "", "")),
        ReceiptHeader(1, ReceiptStatus.FAILED, Shop(124235523, "", "", "245.54"), Meta(124235523, "", "", "")),
        ReceiptHeader(2, ReceiptStatus.FAILED, Shop(164367457, "", "", "1135.54"), Meta(4363757, "", "", "")),
        ReceiptHeader(3, ReceiptStatus.FAILED, Shop(168576823, "", "", "995.54"), Meta(3425425, "", "", "")),
        ReceiptHeader(4, ReceiptStatus.FAILED, Shop(165374575, "", "", "2345.54"), Meta(23432520, "", "", ""))
    ).apply { sortByDescending { it.shop.date } }

}