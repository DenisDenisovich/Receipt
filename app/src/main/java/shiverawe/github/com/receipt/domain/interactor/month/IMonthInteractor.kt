package shiverawe.github.com.receipt.domain.interactor.month

import shiverawe.github.com.receipt.domain.entity.BaseResult
import shiverawe.github.com.receipt.domain.entity.base.ReceiptHeader

interface IMonthInteractor {

    suspend fun getMonthReceipt(dateFrom: Long): BaseResult<List<ReceiptHeader>>
}