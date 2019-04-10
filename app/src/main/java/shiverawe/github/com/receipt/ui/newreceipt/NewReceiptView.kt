package shiverawe.github.com.receipt.ui.newreceipt

interface NewReceiptView {
    fun openReceipt(options: String)
    fun openManual()
    fun openQr()
    fun showProgress()
    fun hideProgress()
    fun onError()
    fun onError(message: String)
    fun onBackPressedIsHandled(): Boolean
}