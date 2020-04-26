package shiverawe.github.com.receipt.data.repository

import io.reactivex.Single
import retrofit2.HttpException
import shiverawe.github.com.receipt.data.bd.datasource.month.IMonthDatabase
import shiverawe.github.com.receipt.data.network.datasource.month.IMonthNetwork
import shiverawe.github.com.receipt.data.network.utils.IUtilsNetwork
import shiverawe.github.com.receipt.domain.repository.IMonthRepository
import shiverawe.github.com.receipt.domain.entity.dto.Receipt
import shiverawe.github.com.receipt.domain.entity.dto.ReceiptHeader
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MonthRepository(
        private val network: IMonthNetwork,
        private val db: IMonthDatabase,
        private val utils: IUtilsNetwork
) : IMonthRepository {

    private val formatter = SimpleDateFormat("yyyyMMdd'T'HHmm", Locale.getDefault())

    override fun getMonthReceipt(dateFrom: Long, dateTo: Long): Single<ArrayList<ReceiptHeader>> =
        network.getMonthReceipts(formatter.format(dateFrom), formatter.format(dateTo))
                .flatMap { networkReceipts ->
                    db.updateMonthCache(dateFrom, dateTo, networkReceipts)
                }
                .map { receipts -> mapToMonthReceipt(receipts) }
                .onErrorResumeNext {
                    if (it is HttpException || !utils.isOnline()) {
                        db.getReceipts(dateFrom, dateTo)
                                .map { receipts -> mapToMonthReceipt(receipts) }
                    } else {
                        Single.error(it)
                    }
                }

    private fun mapToMonthReceipt(receipts: ArrayList<Receipt>): ArrayList<ReceiptHeader> =
        ArrayList(receipts.map { ReceiptHeader(it.receiptId, it.shop, it.meta) })
}