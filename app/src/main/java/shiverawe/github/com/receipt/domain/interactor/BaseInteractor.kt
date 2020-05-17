package shiverawe.github.com.receipt.domain.interactor

import kotlinx.coroutines.CancellationException
import retrofit2.HttpException
import shiverawe.github.com.receipt.domain.entity.BaseResult
import shiverawe.github.com.receipt.domain.entity.ErrorType
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

open class BaseInteractor {

    protected fun isCancel(e: Exception) = e is CancellationException

    protected fun isOffline(e: Exception) = e is UnknownHostException ||
        e is SocketTimeoutException ||
        e is ConnectException

    protected fun isNetwork(e: Exception) = e is HttpException || isOffline(e)

    protected fun <T> Exception.toBaseResult(): BaseResult<T> =
        if (isCancel(this)) {
            BaseResult(isCancel = true)
        } else {
            BaseResult(this, if (isOffline(this)) ErrorType.OFFLINE else ErrorType.ERROR)
        }
}