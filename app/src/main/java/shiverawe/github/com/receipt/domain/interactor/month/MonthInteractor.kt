package shiverawe.github.com.receipt.domain.interactor.month

import shiverawe.github.com.receipt.data.network.utils.isOffline
import shiverawe.github.com.receipt.domain.entity.ErrorType
import shiverawe.github.com.receipt.domain.entity.BaseResult
import shiverawe.github.com.receipt.domain.entity.base.ReceiptHeader
import shiverawe.github.com.receipt.domain.entity.base.ReceiptStatus
import shiverawe.github.com.receipt.domain.interactor.BaseInteractor
import shiverawe.github.com.receipt.domain.repository.IMonthRepository
import java.lang.Exception
import java.util.*

class MonthInteractor(private val repository: IMonthRepository) : BaseInteractor(), IMonthInteractor {

    private val calendar = GregorianCalendar()

    override suspend fun getMonthReceipt(dateFrom: Long): BaseResult<List<ReceiptHeader>> {
        val dateTo = getMonthEndTime(dateFrom)

        if (isOffline()) {
            return getDbReceiptsOnError(dateFrom, dateTo, errorType = ErrorType.OFFLINE)
        }

        return try {
            // Get receipts from network and update db cache
            val networkReceipts = repository.getNetworkReceipt(dateFrom, dateTo)
                .filter { it.status == ReceiptStatus.LOADED }
                .sortedByDescending { it.meta.t }

            repository.updateCache(dateFrom, dateTo, networkReceipts)
            BaseResult(networkReceipts)
        } catch (e: Exception) {
            when {
                isCancel(e) -> BaseResult(isCancel = true)
                isNetwork(e) || isOffline() -> getDbReceiptsOnError(dateFrom, dateTo, e, ErrorType.OFFLINE)
                else -> BaseResult(e, ErrorType.ERROR)
            }
        }
    }

    private suspend fun getDbReceiptsOnError(
        dateFrom: Long,
        dateTo: Long,
        error: Throwable? = null,
        errorType: ErrorType
    ): BaseResult<List<ReceiptHeader>> =
        try {
            BaseResult(repository.getReceiptFromDb(dateFrom, dateTo), error, errorType)
        } catch (e: Exception) {
            e.toBaseResult()
        }

    private fun getMonthEndTime(startDate: Long): Long {
        calendar.timeInMillis = startDate
        calendar.add(Calendar.MONTH, 1)

        return calendar.timeInMillis
    }
}