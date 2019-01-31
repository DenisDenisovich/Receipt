package shiverawe.github.com.receipt.ui.receipt.network

interface ReceiptNetwork {
    fun openQrReader()
    fun openManualInput()
    fun moveBackToManual()
    fun openReceiptFragment(qrData: String, isManualInput: Boolean)
    fun receiptIsSaved(date: Long)
}
