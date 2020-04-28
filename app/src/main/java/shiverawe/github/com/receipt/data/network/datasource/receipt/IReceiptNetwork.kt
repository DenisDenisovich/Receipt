package shiverawe.github.com.receipt.data.network.datasource.receipt

import io.reactivex.Single
import shiverawe.github.com.receipt.data.network.entity.create.CreateResponce
import shiverawe.github.com.receipt.domain.entity.dto.Product
import shiverawe.github.com.receipt.domain.entity.dto.Receipt

interface IReceiptNetwork {

    fun getReceipt(options: Map<String, String>): Single<Receipt?>

    fun getProducts(id: Long): Single<ArrayList<Product>>

    fun saveReceipt(): Single<CreateResponce>
}