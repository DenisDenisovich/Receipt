package shiverawe.github.com.receipt.domain.interactor.create_receipt

import shiverawe.github.com.receipt.data.network.utils.isOffline
import shiverawe.github.com.receipt.domain.entity.ErrorType
import shiverawe.github.com.receipt.domain.entity.ReceiptResult
import shiverawe.github.com.receipt.domain.entity.ReceiptError
import shiverawe.github.com.receipt.domain.entity.base.Meta
import shiverawe.github.com.receipt.domain.entity.base.ReceiptHeader
import shiverawe.github.com.receipt.domain.repository.IReceiptRepository
import shiverawe.github.com.receipt.utils.toLongWithSeconds
import java.lang.Exception
import java.util.concurrent.CancellationException

class CreateReceiptInteractor(private val repository: IReceiptRepository) : ICreateReceiptInteractor {

    override suspend fun createReceipt(qrRawData: String): ReceiptResult<ReceiptHeader> =
        try {
            val meta = parseQrCode(qrRawData)
            createReceiptNetwork(meta)
        } catch (e: ParseQrException) {
            ReceiptResult(error = ReceiptError(throwable = e, type = ErrorType.ERROR))
        }

    override suspend fun createReceipt(meta: Meta): ReceiptResult<ReceiptHeader> = createReceiptNetwork(meta)

    // Go to network for creation receipt
    private suspend fun createReceiptNetwork(meta: Meta): ReceiptResult<ReceiptHeader> {
        if (isOffline()) {
            return ReceiptResult(error = ReceiptError(type = ErrorType.OFFLINE))
        }

        return try {
            ReceiptResult(repository.createReceipt(meta))
        } catch (e: CancellationException) {
            ReceiptResult(isCancel = true)
        } catch (e: Exception) {
            ReceiptResult(error = ReceiptError(throwable = e, type = ErrorType.ERROR))
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