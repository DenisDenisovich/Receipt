package shiverawe.github.com.receipt.data.repository

import io.reactivex.Single
import shiverawe.github.com.receipt.data.bd.ReceiptDatabase
import shiverawe.github.com.receipt.data.network.ReceiptNetwork
import shiverawe.github.com.receipt.data.network.entity.create.CreateResponce
import shiverawe.github.com.receipt.domain.repository.IReceiptRepository
import shiverawe.github.com.receipt.domain.entity.receipt.base.Receipt

class ReceiptRepository: IReceiptRepository {
    private val db = ReceiptDatabase()
    private val network = ReceiptNetwork()

    override fun getReceipt(options: Map<String, String>): Single<Receipt?> {
        return network.getReceipt(options)
    }

    override fun saveReceipt(): Single<CreateResponce> {
        return network.saveReceipt()
    }

    override fun getReceiptById(receiptId: Long): Single<Receipt> {
        return db.getReceiptById(receiptId)
    }
}