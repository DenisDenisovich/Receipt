package shiverawe.github.com.receipt.utils

import android.view.View

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.isVisible() = visibility == View.VISIBLE

fun View.isGone() = visibility == View.GONE

fun View.isInvisible() = visibility == View.INVISIBLE


