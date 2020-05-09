package shiverawe.github.com.receipt.data.network.datasource.receipt

import io.reactivex.Single
import shiverawe.github.com.receipt.domain.entity.base.Meta
import shiverawe.github.com.receipt.domain.entity.base.Product
import shiverawe.github.com.receipt.domain.entity.base.Receipt
import shiverawe.github.com.receipt.domain.entity.base.ReceiptHeader

interface IReceiptNetwork {

    fun getReceipt(meta: Meta): Single<Receipt?>

    fun getProducts(id: Long): Single<List<Product>>

    suspend fun saveReceipt(meta: Meta): ReceiptHeader
}