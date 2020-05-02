package shiverawe.github.com.receipt.utils

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

fun Fragment.toast(string: String) {
    Toast.makeText(requireContext(), string, Toast.LENGTH_LONG).show()
}

fun Fragment.toast(@StringRes stringId: Int) {
    Toast.makeText(requireContext(), getString(stringId), Toast.LENGTH_LONG).show()
}