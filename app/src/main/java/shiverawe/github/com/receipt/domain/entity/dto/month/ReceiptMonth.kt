package shiverawe.github.com.receipt.domain.entity.dto.month

import shiverawe.github.com.receipt.domain.entity.dto.base.Meta
import shiverawe.github.com.receipt.domain.entity.dto.base.Shop

data class ReceiptMonth(var receiptId: Long,
                        val shop: Shop,
                        val meta: Meta)