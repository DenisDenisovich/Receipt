package shiverawe.github.com.receipt.utils

import android.content.Context
import android.content.res.Resources

fun Int.toPixels() = (this * Resources.getSystem().displayMetrics.density).toInt()

fun Int.toDp() = (this / Resources.getSystem().displayMetrics.density).toInt()

fun getBottomNavigationBarHeight(context: Context): Int {
    var statusBarHeight = 0
    val resourceId = context.resources.getIdentifier("navigation_bar_height", "dimen", "android")
    if (resourceId > 0) {
        statusBarHeight = context.resources.getDimensionPixelSize(resourceId)
    }

    return statusBarHeight
}

fun getStatusBarHeight(context: Context): Int {
    var statusBarHeight = 0
    val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        statusBarHeight = context.resources.getDimensionPixelSize(resourceId)
    }

    return statusBarHeight
}
