package shiverawe.github.com.receipt.data.repository

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import shiverawe.github.com.receipt.data.bd.ReceiptDatabase
import shiverawe.github.com.receipt.data.bd.utils.MapperDb
import shiverawe.github.com.receipt.data.network.ReceiptNetwork
import shiverawe.github.com.receipt.data.network.utils.MapperNetwork
import shiverawe.github.com.receipt.data.network.entity.create.CreateRequest
import shiverawe.github.com.receipt.data.network.entity.create.CreateResponce
import shiverawe.github.com.receipt.entity.Receipt
import shiverawe.github.com.receipt.ui.App

class ReceiptRepository {
    private val db = ReceiptDatabase()
    private val network = ReceiptNetwork()

    fun getReceipt(options: Map<String, String>): Single<Receipt?> {
        return network.getReceipt(options)
    }

    fun saveReceipt(): Single<CreateResponce> {
        return network.saveReceipt()
    }

    fun getReceiptById(receiptId: Long): Single<Receipt> {
        return db.getReceiptById(receiptId)
    }
}