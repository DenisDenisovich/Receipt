package shiverawe.github.com.receipt.data.bd.utils

import shiverawe.github.com.receipt.domain.entity.dto.Receipt
import shiverawe.github.com.receipt.domain.entity.dto.ReceiptHeader

interface ICacheDiffUtility {

    fun findDiffReceipts(oldReceipts: ArrayList<Receipt>, newReceipts: ArrayList<Receipt>): Pair<List<Long>,ArrayList<Receipt>>

    fun findDiffReceiptsHeader(oldReceipts: ArrayList<ReceiptHeader>, newReceipts: ArrayList<ReceiptHeader>): Pair<List<Long>,ArrayList<ReceiptHeader>>
}