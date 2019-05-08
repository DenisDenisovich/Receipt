package shiverawe.github.com.receipt.data.network

import io.reactivex.Single
import shiverawe.github.com.receipt.data.network.entity.create.CreateResponce
import shiverawe.github.com.receipt.domain.entity.receipt.base.Receipt

interface IReceiptNetwork {
    fun getReceipt(options: Map<String, String>): Single<Receipt?>
    fun saveReceipt(): Single<CreateResponce>
}