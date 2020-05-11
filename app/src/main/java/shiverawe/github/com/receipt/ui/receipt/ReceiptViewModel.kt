package shiverawe.github.com.receipt.ui.receipt

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import shiverawe.github.com.receipt.domain.entity.ErrorType
import shiverawe.github.com.receipt.domain.entity.base.Receipt
import shiverawe.github.com.receipt.domain.entity.base.ReceiptHeader
import shiverawe.github.com.receipt.domain.interactor.receipt.IReceiptInteractor

class ReceiptViewModel(private val interactor: IReceiptInteractor) : ViewModel() {

    val receiptData: MutableLiveData<Receipt> = MutableLiveData()
    val errorData: MutableLiveData<ErrorType> = MutableLiveData()

    private var currentJob: Job? = null

    fun loadReceipt(id: Long) {
        currentJob?.cancel()
        currentJob = viewModelScope.launch {
            // get header from db
            interactor.getReceiptHeader(id).result?.let {
                receiptData.value = Receipt(it, arrayListOf())
            }

            // get full receipt
            val receipt = interactor.getReceipt(id)
            receipt.result?.let {
                receiptData.value = it
            }
            receipt.error?.let {
                errorData.value = it.type
            }
        }
    }

    fun loadReceipt(receiptHeader: ReceiptHeader) {
        receiptData.value = Receipt(receiptHeader, arrayListOf())
        currentJob?.cancel()
        currentJob = viewModelScope.launch {
            // set receipt Header
            receiptData.value = Receipt(receiptHeader, arrayListOf())

            // get full receipt
            val products = interactor.getProducts(receiptHeader.receiptId)
            products.result?.let {
                receiptData.value = Receipt(receiptHeader, it)
            }
            products.error?.let {
                errorData.value = it.type
            }
        }
    }

    fun getSharedReceipt(receipt: Receipt): String = interactor.getSharedReceipt(receipt)

    fun onClose() {
        currentJob?.cancel()
    }
}