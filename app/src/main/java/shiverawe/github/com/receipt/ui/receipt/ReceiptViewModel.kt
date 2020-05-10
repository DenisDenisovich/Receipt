package shiverawe.github.com.receipt.ui.receipt

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import shiverawe.github.com.receipt.domain.entity.base.Receipt
import shiverawe.github.com.receipt.domain.entity.base.ReceiptHeader
import shiverawe.github.com.receipt.domain.repository.IReceiptRepository

class ReceiptViewModel(private val repository: IReceiptRepository): ViewModel() {

    val receipt: MutableLiveData<Receipt> = MutableLiveData()

    private var currentJob: Job? = null

    fun getReceipt(id: Long) {
        currentJob?.cancel()
        currentJob = viewModelScope.launch {

        }
    }

    fun getReceipt(meta: String) {

    }

    fun getReceipt(receiptHeader: ReceiptHeader) {

    }

    fun  onClose() {
        currentJob?.cancel()
    }
}