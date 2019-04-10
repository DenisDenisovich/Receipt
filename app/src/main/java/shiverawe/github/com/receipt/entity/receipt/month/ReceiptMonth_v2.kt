package shiverawe.github.com.receipt.entity.receipt.month

import shiverawe.github.com.receipt.entity.receipt.base.Meta
import shiverawe.github.com.receipt.entity.receipt.base.Shop

data class ReceiptMonth_v2(var receiptId: Long,
                        val shop: Shop,
                        val meta: Meta)