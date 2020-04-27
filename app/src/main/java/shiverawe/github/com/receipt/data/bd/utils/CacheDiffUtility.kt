package shiverawe.github.com.receipt.data.bd.utils

import com.google.gson.Gson
import shiverawe.github.com.receipt.domain.entity.dto.Receipt
import shiverawe.github.com.receipt.domain.entity.dto.ReceiptHeader

class CacheDiffUtility: ICacheDiffUtility {

    /**
     * Compare two list.
     * [Pair.first] - ids there are not on [newReceipts]
     * [Pair.second] - list of [Receipt], that are not on [oldReceipts]
     **/
    override fun findDiffReceipts(
        oldReceipts: ArrayList<Receipt>,
        newReceipts: ArrayList<Receipt>
    ): Pair<List<Long>,ArrayList<Receipt>> {
        val localHash = HashMap<String, Long>()
        val addedReceipts = ArrayList<Receipt>()
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

    /**
     * Compare two list.
     * [Pair.first] - ids there are not on [newReceipts]
     * [Pair.second] - list of [ReceiptHeader], that are not on [oldReceipts]
     **/
    override fun findDiffReceiptsHeader(
        oldReceipts: ArrayList<ReceiptHeader>,
        newReceipts: ArrayList<ReceiptHeader>
    ): Pair<List<Long>, ArrayList<ReceiptHeader>> {
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

    private fun getKey(receipt: Receipt): String {
        val products = ArrayList(receipt.items.sortedBy { it.text })
        val keyReceipt = Receipt(0, receipt.shop, receipt.meta, products)
        return Gson().toJson(keyReceipt)
    }

    private fun getKey(receipt: ReceiptHeader): String {
        val keyReceipt = ReceiptHeader(0, receipt.shop, receipt.meta)
        return Gson().toJson(keyReceipt)
    }
}