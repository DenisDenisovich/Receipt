package shiverawe.github.com.receipt.data.repository

import io.reactivex.Single
import retrofit2.HttpException
import shiverawe.github.com.receipt.data.bd.datasource.month.IMonthDatabase
import shiverawe.github.com.receipt.data.network.datasource.month.IMonthNetwork
import shiverawe.github.com.receipt.data.network.utils.IUtilsNetwork
import shiverawe.github.com.receipt.domain.repository.IMonthRepository
import shiverawe.github.com.receipt.domain.entity.dto.ReceiptHeader
import java.text.SimpleDateFormat
import java.util.*

class MonthRepository(
        private val network: IMonthNetwork,
        private val db: IMonthDatabase,
        private val utils: IUtilsNetwork
) : IMonthRepository {

    private val formatter = SimpleDateFormat("yyyyMMdd'T'HHmm", Locale.getDefault())

    override fun getMonthReceipt(dateFrom: Long, dateTo: Long): Single<List<ReceiptHeader>> =
        network.getMonthReceipts(formatter.format(dateFrom), formatter.format(dateTo))
                .flatMap { networkReceipts ->
                    db.updateMonthCache(dateFrom, dateTo, networkReceipts)
                }
                .onErrorResumeNext {
                    if (it is HttpException || !utils.isOnline()) {
                        db.getReceiptHeaders(dateFrom, dateTo)
                    } else {
                        Single.error(it)
                    }
                }
}