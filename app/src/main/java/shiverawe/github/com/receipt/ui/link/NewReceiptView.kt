package shiverawe.github.com.receipt.ui.link

interface NewReceiptView {
    fun openReceipt(options: String)
    fun showProgress()
    fun hideProgress()
    fun onError()
    fun onError(message: String)
    fun onBackPressedIsHandled(): Boolean
}