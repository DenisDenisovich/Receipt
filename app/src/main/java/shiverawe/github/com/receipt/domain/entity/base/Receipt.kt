package shiverawe.github.com.receipt.domain.entity.base

data class Receipt(
    var header: ReceiptHeader,
    val items: List<Product>
)