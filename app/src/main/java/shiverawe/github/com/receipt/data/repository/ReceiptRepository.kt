package shiverawe.github.com.receipt.data.repository

import io.reactivex.Single
import shiverawe.github.com.receipt.data.bd.datasource.receipt.IReceiptDatabase
import shiverawe.github.com.receipt.data.network.datasource.receipt.IReceiptNetwork
import shiverawe.github.com.receipt.data.network.entity.create.CreateResponce
import shiverawe.github.com.receipt.domain.repository.IReceiptRepository
import shiverawe.github.com.receipt.domain.entity.dto.base.Receipt

class ReceiptRepository(
        private val db: IReceiptDatabase,
        private val network: IReceiptNetwork
) : IReceiptRepository {
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