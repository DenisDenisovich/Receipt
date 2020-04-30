package shiverawe.github.com.receipt.data.network.datasource.receipt

import io.reactivex.Single
import shiverawe.github.com.receipt.data.network.entity.create.CreateResponce
import shiverawe.github.com.receipt.domain.entity.dto.Meta
import shiverawe.github.com.receipt.domain.entity.dto.Product
import shiverawe.github.com.receipt.domain.entity.dto.Receipt

interface IReceiptNetwork {

    fun getReceipt(meta: Meta): Single<Receipt?>

    fun getProducts(id: Long): Single<List<Product>>

    fun saveReceipt(): Single<CreateResponce>
}