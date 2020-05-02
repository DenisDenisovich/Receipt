package shiverawe.github.com.receipt.utils

import java.text.SimpleDateFormat
import java.util.*

private val dateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale("ru"))

fun String.toLongWithSeconds(): Long = dateFormatter.parse(this)?.time ?: 0L