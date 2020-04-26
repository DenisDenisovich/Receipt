package shiverawe.github.com.receipt.data.bd.utils

import shiverawe.github.com.receipt.domain.entity.dto.Receipt

interface ICacheDiffUtility {
    fun findDiffReceipts(localReceipts: ArrayList<Receipt>, networkReceipts: ArrayList<Receipt>): Pair<List<Long>,ArrayList<Receipt>>
}