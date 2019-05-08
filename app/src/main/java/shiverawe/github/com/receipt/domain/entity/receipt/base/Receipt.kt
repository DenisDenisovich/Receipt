package shiverawe.github.com.receipt.domain.entity.receipt.base

data class Receipt(
        var receiptId: Long,
        val shop: Shop,
        val meta: Meta,
        val items: ArrayList<Product>)