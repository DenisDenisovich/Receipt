package shiverawe.github.com.receipt.utils

import kotlinx.coroutines.CancellationException
import retrofit2.HttpException
import shiverawe.github.com.receipt.data.network.utils.isOffline
import shiverawe.github.com.receipt.domain.entity.BaseResult
import shiverawe.github.com.receipt.domain.entity.ErrorType

fun Exception.isCancel(): Boolean = this is CancellationException

fun Exception.isNetwork() = this is HttpException

fun <T> Exception.toBaseResult(checkOfflineError: Boolean = true): BaseResult<T> =
    if (this.isCancel()) {
        BaseResult(isCancel = true)
    } else {
        val errorType = if (checkOfflineError &&  (this.isNetwork() || isOffline())) ErrorType.OFFLINE
        else ErrorType.ERROR

        BaseResult(this, errorType)
    }