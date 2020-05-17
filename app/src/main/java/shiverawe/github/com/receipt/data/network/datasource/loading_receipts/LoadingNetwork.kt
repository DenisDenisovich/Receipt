package shiverawe.github.com.receipt.data.network.datasource.loading_receipts

import shiverawe.github.com.receipt.data.network.api.Api
import shiverawe.github.com.receipt.data.network.entity.receipt.ReceiptRequest
import shiverawe.github.com.receipt.data.network.mapper.toReceiptHeader
import shiverawe.github.com.receipt.domain.entity.base.ReceiptHeader

class LoadingNetwork(val api: Api): ILoadingNetwork {

    override suspend fun getReceipts(ids: List<Long>): List<ReceiptHeader> =
        api.getReceipts(ReceiptRequest(ids = ids)).toReceiptHeader()
}