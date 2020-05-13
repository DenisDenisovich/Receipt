package shiverawe.github.com.receipt.domain.interactor.month

import retrofit2.HttpException
import shiverawe.github.com.receipt.data.network.utils.isOffline
import shiverawe.github.com.receipt.domain.entity.ErrorType
import shiverawe.github.com.receipt.domain.entity.ReceiptError
import shiverawe.github.com.receipt.domain.entity.ReceiptResult
import shiverawe.github.com.receipt.domain.entity.base.ReceiptHeader
import shiverawe.github.com.receipt.domain.entity.base.ReceiptStatus
import shiverawe.github.com.receipt.domain.repository.IMonthRepository
import java.lang.Exception
import java.util.*
import java.util.concurrent.CancellationException

class MonthInteractor(private val repository: IMonthRepository) : IMonthInteractor {

    private val gregorianCalendar = GregorianCalendar()

    override suspend fun getMonthReceipt(dateFrom: Long): ReceiptResult<List<ReceiptHeader>> {
        val dateTo = getMonthEndTime(dateFrom)

        if (isOffline()) {
            return getDbReceiptsOnError(dateFrom, dateTo, errorType = ErrorType.OFFLINE)
        }

        return try {
            ReceiptResult(getNetworkReceipts(dateFrom, dateTo))
        } catch (e: CancellationException) {
            // Coroutine is cancelled, return nothing
            ReceiptResult(isCancel = true)
        } catch (e: Exception) {
            if (e is HttpException || isOffline()) {
                getDbReceiptsOnError(dateFrom, dateTo, e, ErrorType.OFFLINE)
            } else {
                ReceiptResult<List<ReceiptHeader>>(error = ReceiptError(e, ErrorType.ERROR))
            }
        }
    }

    private suspend fun getNetworkReceipts(dateFrom: Long, dateTo: Long): List<ReceiptHeader> {
        // Get receipts from network and update db cache
        val networkReceipts = repository.getNetworkReceipt(dateFrom, dateTo)
            .filter { it.status == ReceiptStatus.LOADED }

        repository.updateCache(dateFrom, dateTo, networkReceipts)

        return networkReceipts
    }

    private suspend fun getDbReceiptsOnError(
        dateFrom: Long,
        dateTo: Long,
        error: Throwable? = null,
        errorType: ErrorType
    ): ReceiptResult<List<ReceiptHeader>> {
        return try {
            val dbReceipts = repository.getReceiptFromDb(dateFrom, dateTo)
            ReceiptResult(dbReceipts, ReceiptError(error, errorType))
        } catch (e: Exception) {
            // Error while getting data from db. Return error without data
            ReceiptResult<List<ReceiptHeader>>(error = ReceiptError(e, ErrorType.ERROR))
        }
    }

    private fun getMonthEndTime(startDate: Long): Long =
        gregorianCalendar.apply {
            timeInMillis = startDate
            add(Calendar.MONTH, 1)
        }.timeInMillis
}