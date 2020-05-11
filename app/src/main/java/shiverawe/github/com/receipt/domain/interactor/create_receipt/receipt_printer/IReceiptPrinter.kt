package shiverawe.github.com.receipt.domain.interactor.create_receipt.receipt_printer

import shiverawe.github.com.receipt.domain.entity.base.Receipt

interface IReceiptPrinter {

    fun receiptToString(receipt: Receipt): String
}