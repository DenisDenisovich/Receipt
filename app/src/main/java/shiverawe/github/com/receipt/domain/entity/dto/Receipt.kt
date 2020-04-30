package shiverawe.github.com.receipt.domain.entity.dto

data class Receipt(
    var header: ReceiptHeader,
    val items: List<Product>
)