package shiverawe.github.com.receipt.ui

interface BackPressedHandle {

    // return true if component can't handle action
    fun canGoBack(): Boolean
}