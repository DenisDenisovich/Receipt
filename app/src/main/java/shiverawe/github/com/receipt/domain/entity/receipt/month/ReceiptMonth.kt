package shiverawe.github.com.receipt.domain.entity.receipt.month

import shiverawe.github.com.receipt.domain.entity.receipt.base.Meta
import shiverawe.github.com.receipt.domain.entity.receipt.base.Shop

data class ReceiptMonth(var receiptId: Long,
                        val shop: Shop,
                        val meta: Meta)