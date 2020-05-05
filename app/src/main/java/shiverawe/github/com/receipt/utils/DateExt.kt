package shiverawe.github.com.receipt.utils

import java.text.SimpleDateFormat
import java.util.*

private val dateFormatterWithMilliseconds = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale("ru"))
private val dateFormatterWithSeconds= SimpleDateFormat("yyyyMMdd'T'HHmm", Locale("ru"))

fun String.toLongWithMilliseconds(): Long = dateFormatterWithMilliseconds.parse(this)?.time ?: 0L

fun String.toLongWithSeconds(): Long = dateFormatterWithSeconds.parse(this)?.time ?: 0L

fun Long.toStringWithMilliseconds() = dateFormatterWithMilliseconds.format(this)

fun Long.toStringWithSeconds(): String = dateFormatterWithSeconds.format(this)