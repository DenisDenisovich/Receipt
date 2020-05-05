package shiverawe.github.com.receipt.domain.interactor.create_receipt

import shiverawe.github.com.receipt.domain.entity.base.ErrorType
import shiverawe.github.com.receipt.domain.entity.base.Meta

interface CreateReceiptListener {

    fun onSuccess(id: Long, meta: Meta)

    fun onError(error: Throwable? = null, errorType: ErrorType)
}