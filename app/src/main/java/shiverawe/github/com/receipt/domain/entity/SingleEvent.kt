package shiverawe.github.com.receipt.domain.entity

data class SingleEvent<T>(val value: T) {

    var isHandled = false

    fun getValueFirstTime(): T? =
        if (isHandled) {
            null
        } else {
            isHandled = true
            value
        }
}