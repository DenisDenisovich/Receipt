package shiverawe.github.com.receipt.utils

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

fun Fragment.toast(string: String, isLongDuration: Boolean = true): Toast {
    val toast = Toast.makeText(
        requireContext(),
        string,
        if (isLongDuration) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
    )
    toast.show()

    return toast
}

fun Fragment.toast(@StringRes stringId: Int, isLongDuration: Boolean = true): Toast {
    val toast = Toast.makeText(
        requireContext(),
        getString(stringId),
        if (isLongDuration) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
    )
    toast.show()

    return toast
}