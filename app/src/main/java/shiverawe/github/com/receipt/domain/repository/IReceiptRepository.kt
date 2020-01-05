package shiverawe.github.com.receipt.domain.repository

import io.reactivex.Single
import shiverawe.github.com.receipt.data.network.entity.create.CreateResponse
import shiverawe.github.com.receipt.domain.entity.dto.base.Receipt

interface IReceiptRepository {

    fun getReceipt(options: Map<String, String>): Single<Receipt?>

    fun saveReceipt(): Single<CreateResponse>

    fun getReceiptById(receiptId: Long): Single<Receipt>

}
