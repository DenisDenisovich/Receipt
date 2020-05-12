package shiverawe.github.com.receipt.ui.history.month

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import shiverawe.github.com.receipt.domain.entity.ErrorType
import shiverawe.github.com.receipt.domain.entity.base.ReceiptHeader
import shiverawe.github.com.receipt.domain.interactor.month.IMonthInteractor

class MonthViewModel(private val interactor: IMonthInteractor): ViewModel() {

    val sum: MutableLiveData<String> = MutableLiveData()
    val receipts: MutableLiveData<List<ReceiptHeader>> = MutableLiveData()
    val error: MutableLiveData<ErrorType> = MutableLiveData()

    fun loadReceipts(date: Long) {

    }

    fun refreshReceipts(date: Long) {

    }
}