package shiverawe.github.com.receipt.domain.interactor.create_receipt

import shiverawe.github.com.receipt.domain.entity.dto.ErrorType
import shiverawe.github.com.receipt.domain.entity.dto.Meta

interface CreateReceiptListener {

    fun onSuccess(id: Long, meta: Meta)

    fun onError(error: Throwable? = null, errorType: ErrorType)
}