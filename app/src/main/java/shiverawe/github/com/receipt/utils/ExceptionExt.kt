package shiverawe.github.com.receipt.utils

import kotlinx.coroutines.CancellationException
import retrofit2.HttpException
import shiverawe.github.com.receipt.domain.entity.BaseResult
import shiverawe.github.com.receipt.domain.entity.ErrorType
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun Exception.isCancel(): Boolean = this is CancellationException

fun Exception.isNetwork() = this is HttpException || isOffline()

fun Exception.isOffline() = this is UnknownHostException ||
    this is SocketTimeoutException ||
    this is ConnectException

fun <T> Exception.toBaseResult(): BaseResult<T> =
    if (this.isCancel()) {
        BaseResult(isCancel = true)
    } else {
        BaseResult(this, if (isOffline()) ErrorType.OFFLINE else ErrorType.ERROR)
    }