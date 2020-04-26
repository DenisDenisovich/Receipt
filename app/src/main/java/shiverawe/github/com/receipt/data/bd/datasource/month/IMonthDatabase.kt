package shiverawe.github.com.receipt.data.bd.datasource.month

import io.reactivex.Single
import shiverawe.github.com.receipt.domain.entity.dto.Receipt

interface IMonthDatabase {
    fun updateMonthCache(dateFrom: Long, dateTo: Long, networkReceipts: ArrayList<Receipt>): Single<ArrayList<Receipt>>
    fun getReceipts(dataFrom: Long, dataTo: Long): Single<ArrayList<Receipt>>
}