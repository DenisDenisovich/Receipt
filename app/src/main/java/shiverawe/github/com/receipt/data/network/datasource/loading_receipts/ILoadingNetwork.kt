package shiverawe.github.com.receipt.data.network.datasource.loading_receipts

import shiverawe.github.com.receipt.domain.entity.base.ReceiptHeader

interface ILoadingNetwork {

    suspend fun getReceipts(ids: List<Long>): List<ReceiptHeader>
}