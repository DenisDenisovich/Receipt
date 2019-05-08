package shiverawe.github.com.receipt.data.bd.utils

import com.google.gson.Gson
import shiverawe.github.com.receipt.domain.entity.receipt.base.Receipt

class CacheDiffUtility: ICacheDiffUtility {
    override fun findDiffReceipts(localReceipts: ArrayList<Receipt>, networkReceipts: ArrayList<Receipt>): Pair<List<Long>,ArrayList<Receipt>> {
        val localHash = HashMap<String, Long>()
        val newNetwork = ArrayList<Receipt>()
        localReceipts.forEach {
            receipt ->
            val key = getKey(receipt)
            localHash[key] = receipt.receiptId
        }
        networkReceipts.forEach {
            networkReceipt ->
            val value = localHash.remove(getKey(networkReceipt))
            if (value == null) newNetwork.add(networkReceipt)
        }
        val deletedIds = localHash.map { it.value }
        return Pair(deletedIds, newNetwork)
    }


    private fun getKey(receipt: Receipt): String {
        val products = ArrayList(receipt.items.sortedBy { it.text })
        val keyReceipt = Receipt(0, receipt.shop, receipt.meta, products)
        return Gson().toJson(keyReceipt)
    }
}