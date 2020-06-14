package shiverawe.github.com.receipt.domain.entity

data class SingleEvent<T>(val event: T) {

    private var isHandled = false

    fun getFirstTime(): T? =
        if (isHandled) {
            null
        } else {
            isHandled = true
            event
        }
}