package shiverawe.github.com.receipt.entity

data class Receipt(
        var receiptId: Long,
        val shop: Shop,
        val meta: Meta,
        val items: ArrayList<Product>)