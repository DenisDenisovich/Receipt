package shiverawe.github.com.receipt.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

fun EditText.addTextListener(onChange: (text: String) -> Unit) {

    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            s?.let { text ->
                onChange(text.toString())
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    })
}