package shiverawe.github.com.receipt.data.bd.datasource.receipt

import io.reactivex.Single
import shiverawe.github.com.receipt.domain.entity.dto.Product
import shiverawe.github.com.receipt.domain.entity.dto.Receipt

interface IReceiptDatabase {

    fun updateProductsCache(remoteReceiptId: Long, networkProducts: ArrayList<Product>): Single<Receipt>

    fun getReceiptById(receiptId: Long): Single<Receipt>
}