package shiverawe.github.com.receipt.data.bd.utils

import shiverawe.github.com.receipt.domain.entity.base.ReceiptHeader

interface ICacheDiffUtility {

    fun findDiffReceiptsHeader(oldReceipts: List<ReceiptHeader>, newReceipts: List<ReceiptHeader>): Pair<List<Long>, List<ReceiptHeader>>
}