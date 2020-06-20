package shiverawe.github.com.receipt.ui.receipt.create.root

import shiverawe.github.com.receipt.domain.entity.base.ReceiptHeader

interface CreateReceiptNavigation {
    fun openQr()
    fun openManual()
    fun goBack()
    fun openReceipt(receiptHeader: ReceiptHeader)
    fun receiptIsCreated()
}