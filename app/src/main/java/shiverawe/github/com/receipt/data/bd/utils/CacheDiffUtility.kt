package shiverawe.github.com.receipt.data.bd.utils

import com.google.gson.Gson
import shiverawe.github.com.receipt.domain.entity.base.ReceiptHeader

class CacheDiffUtility: ICacheDiffUtility {

    private val gson = Gson()

    /**
     * Compare two list.
     * [Pair.first] - ids of elements there are not on [newReceipts]
     * [Pair.second] - list of [ReceiptHeader], that are not on [oldReceipts]
     **/
    override fun findDiffReceiptsHeader(
        oldReceipts: List<ReceiptHeader>,
        newReceipts: List<ReceiptHeader>
    ): Pair<List<Long>, List<ReceiptHeader>> {
        val localHash = HashMap<String, Long>()
        val addedReceipts = ArrayList<ReceiptHeader>()
        oldReceipts.forEach { receipt ->
            localHash[getKey(receipt)] = receipt.receiptId
        }
        newReceipts.forEach { receipt ->
            if (localHash.remove(getKey(receipt)) == null) {
                addedReceipts.add(receipt)
            }
        }
        val deletedIds = localHash.map { it.value }
        return Pair(deletedIds, addedReceipts)
    }

    private fun getKey(receipt: ReceiptHeader): String =
        gson.toJson(ReceiptHeader(0, receipt.status, receipt.shop, receipt.meta))
}