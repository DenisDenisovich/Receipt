package shiverawe.github.com.receipt.domain.interactor.month

import shiverawe.github.com.receipt.domain.entity.ReceiptResult
import shiverawe.github.com.receipt.domain.entity.base.ReceiptHeader

interface IMonthInteractor {

    suspend fun getMonthReceipt(dateFrom: Long): ReceiptResult<List<ReceiptHeader>>
}