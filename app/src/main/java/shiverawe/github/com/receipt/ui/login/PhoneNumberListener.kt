package shiverawe.github.com.receipt.ui.login

import android.text.Editable
import android.text.TextWatcher

class PhoneNumberListener(val onReplace: (text: String) -> Unit): TextWatcher {

    private var ignore = false
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        if (count != 0) ignore = true
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    override fun afterTextChanged(s: Editable?) {
        if (ignore) {
            ignore = false
            return
        }
        ignore = true
        onReplace(getFormattedNumber(s.toString()))
    }

    private fun getFormattedNumber(s: String): String {
        val digits = s.replace("\\D".toRegex(), "").removePrefix("7")
        val formatterNumber = StringBuilder("+7-")

        digits.forEachIndexed { index, c ->
            if (index > 9) return@forEachIndexed

            formatterNumber.append(c)

            if (index == 2 || index == 5 || index == 7) {
                if (index == digits.lastIndex) return@forEachIndexed
                formatterNumber.append("-")
            }
        }

        return formatterNumber.toString()
    }
}