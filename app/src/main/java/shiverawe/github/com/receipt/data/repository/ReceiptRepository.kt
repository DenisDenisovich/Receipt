package shiverawe.github.com.receipt.data.repository

import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.HttpException
import shiverawe.github.com.receipt.data.bd.datasource.receipt.IReceiptDatabase
import shiverawe.github.com.receipt.data.network.datasource.receipt.IReceiptNetwork
import shiverawe.github.com.receipt.data.network.entity.create.CreateResponce
import shiverawe.github.com.receipt.data.network.utils.IUtilsNetwork
import shiverawe.github.com.receipt.domain.repository.IReceiptRepository
import shiverawe.github.com.receipt.domain.entity.dto.Receipt

class ReceiptRepository(
    private val db: IReceiptDatabase,
    private val network: IReceiptNetwork,
    private val utils: IUtilsNetwork

) : IReceiptRepository {

    override fun getReceipt(options: Map<String, String>): Single<Receipt?> {
        return network.getReceipt(options)
    }

    override fun saveReceipt(): Single<CreateResponce> {
        return network.saveReceipt()
    }

    override fun getReceiptById(receiptId: Long): Observable<Receipt> {
        // Get receipt from DB. Return this receipt if products list isn't empty
        // Go to network if products list is empty
        val fullReceiptSource: Observable<Receipt> = db.getReceiptById(receiptId)
            .flatMap { dbReceipt ->
                if (dbReceipt.items.isEmpty()) {
                    network.getProducts(receiptId)
                        .flatMap { db.updateProductsCache(receiptId, it) }
                        .onErrorResumeNext {
                            if (it is HttpException || !utils.isOnline()) {
                                db.getReceiptById(receiptId)
                            } else {
                                Single.error(it)
                            }
                        }
                } else {
                    Single.just(dbReceipt)
                }
            }.toObservable()
        // Get receipt header, while fullReceiptSource in progress
        val headerReceiptSource = db.getReceiptHeaderById(receiptId)
            .map { Receipt(it.receiptId, it.shop, it.meta, ArrayList()) }
            .toObservable()
        return Observable.merge(
            headerReceiptSource,
            fullReceiptSource
        )
    }
}