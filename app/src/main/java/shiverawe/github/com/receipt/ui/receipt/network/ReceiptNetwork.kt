package shiverawe.github.com.receipt.ui.receipt.network

interface ReceiptNetwork {
    fun openQrReader()
    fun openManualInput()
    fun openReceiptFragment(qrData: String)
    fun receiptIsSaved(date: Long)
}
