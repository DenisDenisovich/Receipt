package shiverawe.github.com.receipt.utils

import android.content.res.Resources

fun Int.toPixels() = (this * Resources.getSystem().displayMetrics.density).toInt()

fun Int.toDp() = (this / Resources.getSystem().displayMetrics.density).toInt()