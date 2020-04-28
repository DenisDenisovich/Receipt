package shiverawe.github.com.receipt.data.bd.utils

import shiverawe.github.com.receipt.domain.entity.dto.ReceiptHeader

interface ICacheDiffUtility {

    fun findDiffReceiptsHeader(oldReceipts: ArrayList<ReceiptHeader>, newReceipts: ArrayList<ReceiptHeader>): Pair<List<Long>,ArrayList<ReceiptHeader>>
}