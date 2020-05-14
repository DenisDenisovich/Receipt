package shiverawe.github.com.receipt.domain.interactor.create_receipt

import shiverawe.github.com.receipt.data.network.utils.isOffline
import shiverawe.github.com.receipt.domain.entity.ErrorType
import shiverawe.github.com.receipt.domain.entity.BaseResult
import shiverawe.github.com.receipt.domain.entity.base.Meta
import shiverawe.github.com.receipt.domain.entity.base.ReceiptHeader
import shiverawe.github.com.receipt.domain.repository.IReceiptRepository
import shiverawe.github.com.receipt.utils.toBaseResult
import shiverawe.github.com.receipt.utils.toLongWithSeconds
import java.lang.Exception

class CreateReceiptInteractor(private val repository: IReceiptRepository) : ICreateReceiptInteractor {

    override suspend fun createReceipt(qrRawData: String): BaseResult<ReceiptHeader> =
        try {
            val meta = parseQrCode(qrRawData)
            createReceiptNetwork(meta)
        } catch (e: ParseQrException) {
            e.toBaseResult()
        }

    override suspend fun createReceipt(meta: Meta): BaseResult<ReceiptHeader> = createReceiptNetwork(meta)

    // Go to network for creation receipt
    private suspend fun createReceiptNetwork(meta: Meta): BaseResult<ReceiptHeader> {
        if (isOffline()) {
            return BaseResult(ErrorType.OFFLINE)
        }

        return try {
            BaseResult(repository.createReceipt(meta))
        } catch (e: Exception) {
            e.toBaseResult()
        }
    }

    /**
     * Parse raw data from qr code to [Meta]
     * Throw Exception if [qrRawData] does not match the pattern
     * Pattern:
     * t=20200504T180400&s=483.02&fn=9280440300531305&i=32196&fp=3906617997&n=1
     **/
    private fun parseQrCode(qrRawData: String): Meta {
        try {
            val meta = HashMap<String, String>()
            val parameters = qrRawData.split("&")

            parameters.forEach { parameter ->
                val key = parameter.substring(0, parameter.indexOf("="))
                val value = parameter.substring(parameter.indexOf("=") + 1, parameter.length)
                meta[key] = value
            }

            return Meta(
                t = (meta["t"] ?: "").toLongWithSeconds(),
                fn = meta["fn"] ?: "",
                fp = meta["fp"] ?: "",
                fd = meta["i"] ?: "",
                s = meta["s"]?.toDoubleOrNull() ?: 0.0
            )
        } catch (e: Exception) {
            throw ParseQrException(e)
        }
    }
}