package shiverawe.github.com.receipt.ui

interface BackPressedHandle {

    /**
     * @return true, if component allows to close himself
     * @return false, if component forbid to close himself
     **/
    fun quitOnBackPressed(): Boolean
}