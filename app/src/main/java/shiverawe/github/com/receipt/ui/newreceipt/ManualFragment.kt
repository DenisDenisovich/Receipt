package shiverawe.github.com.receipt.ui.newreceipt

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_manual.view.*
import shiverawe.github.com.receipt.R
import java.lang.Exception
import java.lang.StringBuilder

class ManualFragment : Fragment(), View.OnFocusChangeListener {
    private var receiptMeta = ""
    private var textIsValid = false
    private var errorMessage = "нет данных"
    private var textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            changeBtnBackground()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    private var dateTextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            changeBtnBackground()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val newText = changeDateTimeText(s, '.', 8, count)
            newText?.let {
                view!!.et_manual_date.setText(newText)
                view!!.et_manual_date.setSelection(newText.length)
            }
        }
    }


    private var timeTextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            changeBtnBackground()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val newText = changeDateTimeText(s, ':', 5, count)
            newText?.let {
                view!!.et_manual_time.setText(newText)
                view!!.et_manual_time.setSelection(newText.length)
            }
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_manual, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        changeBtnBackground()
        view.btn_manual.setOnClickListener {
            if (textIsValid)
                (parentFragment as NewReceiptView).openReceipt(receiptMeta)
            else
                Toast.makeText(context!!, "Ошибка: $errorMessage", Toast.LENGTH_LONG).show()
        }

        view.btn_manual_back.setOnClickListener {
            activity?.onBackPressed()
        }

        view.et_manual_fd.onFocusChangeListener = this
        view.et_manual_fn.onFocusChangeListener = this
        view.et_manual_fp.onFocusChangeListener = this
        view.et_manual_sum.onFocusChangeListener = this
        view.et_manual_date.onFocusChangeListener = this
        view.et_manual_time.onFocusChangeListener = this
        view.et_manual_fd.addTextChangedListener(textWatcher)
        view.et_manual_fn.addTextChangedListener(textWatcher)
        view.et_manual_fp.addTextChangedListener(textWatcher)
        view.et_manual_sum.addTextChangedListener(textWatcher)
        view.et_manual_date.addTextChangedListener(dateTextWatcher)
        view.et_manual_time.addTextChangedListener(timeTextWatcher)
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        changeEtBackground(view!!.et_manual_fd)
        changeEtBackground(view!!.et_manual_fn)
        changeEtBackground(view!!.et_manual_fp)
        changeEtBackground(view!!.et_manual_sum)
        changeEtBackground(view!!.et_manual_date)
        changeEtBackground(view!!.et_manual_time)
        changeBtnBackground()
    }

    private fun changeEtBackground(view: EditText) {
        if (!view.hasFocus() && view.text.toString().isNotEmpty())
            view.setBackgroundResource(R.drawable.et_manual_finish)
        else
            view.setBackgroundResource(R.drawable.et_manual)
    }

    private fun changeBtnBackground() {
        if (checkData()) {
            view!!.btn_manual.setBackgroundResource(R.drawable.btn_blue)
        } else {
            view!!.btn_manual.setBackgroundResource(R.drawable.btn_gray)
        }
    }

    private fun checkData(): Boolean {
        val fd = view!!.et_manual_fd.text.toString().trim()
        val fn = view!!.et_manual_fn.text.toString().trim()
        val fp = view!!.et_manual_fp.text.toString().trim()
        val s = view!!.et_manual_sum.text.toString().trim()
        val date = view!!.et_manual_date.text.toString().trim()
        val time = view!!.et_manual_time.text.toString().trim()
        if (checkFd(fd) &&
                checkFn(fn) &&
                checkFp(fp) &&
                checkSum(s) &&
                checkDate(date) &&
                checkTime(time)) {
            val t = getDateString(date, time)
            val receiptData = StringBuilder()
            receiptData.append("t=$t&s=$s&fn=$fn&i=$fd&fp=$fp")
            receiptMeta = receiptData.toString()
            textIsValid = true
        } else {
            textIsValid = false
        }
        return textIsValid
    }


    // Template: YYYYMMDDTHHMM
    // Example:  20170605T2101
    private fun getDateString(date: String, time: String): String {
        val dateValues = date.split(".")
        val timeValues = time.split(":")
        val year = "20${dateValues.last()}"
        val month = if (dateValues[1].length == 1) "0${dateValues[1]}" else dateValues[1]
        val day = if (dateValues[0].length == 1) "0${dateValues[0]}" else dateValues[0]
        val hour = if (timeValues[0].length == 1) "0${timeValues[0]}" else timeValues[0]
        val minute = if (timeValues[1].length == 1) "0${timeValues[1]}" else timeValues[1]
        val t = StringBuilder()
        t.append(year)
        t.append(month)
        t.append(day)
        t.append("T")
        t.append(hour)
        t.append(minute)
        return t.toString()
    }

    private fun checkFd(fd: String): Boolean {
        val isCorrect = fd.isNotEmpty()
        if (!isCorrect) errorMessage = "формат ФД неверный"
        return isCorrect
    }

    private fun checkFn(fn: String): Boolean {
        val isCorrect = fn.isNotEmpty()
        if (!isCorrect) errorMessage = "формат ФН неверный"
        return isCorrect
    }

    private fun checkFp(fp: String): Boolean {
        val isCorrect = fp.isNotEmpty()
        if (!isCorrect) errorMessage = "формат ФП неверный"
        return isCorrect
    }


    private fun checkDate(date: String): Boolean {
        var isCorrect = false
        val values = date.split(".")
        if (values.size == 3) {
            try {
                val day = values[0].toInt()
                val month = values[1].toInt()
                val year = values[2].toInt()
                isCorrect = month in 0..12 && day in 0..31 && year in 0..99
            } catch (e: Exception) {
                isCorrect = false
            }
        }
        if (!isCorrect) errorMessage = "формат даты неверный"
        return isCorrect
    }

    private fun checkTime(time: String): Boolean {
        var isCorrect = false
        val values = time.split(":")
        if (values.size == 2) {
            try {
                val hour = values[0].toInt()
                val minute = values[1].toInt()
                isCorrect = hour in 0..23 && minute in 0..59
            } catch (e: Exception) {
                isCorrect = false
            }
        }
        if (!isCorrect) errorMessage = "формат времени неверный"
        return isCorrect
    }

    private fun checkSum(sum: String): Boolean {
        var isCorrect = false
        if (sum.isNotEmpty()) {
            if (sum.contains(".")) {
                // if sum contains not only digits and dots
                if (TextUtils.isDigitsOnly(sum.filter { it != '.' })) {
                    val numberParts = sum.split(".")
                    // if sum contains only one dot
                    if (numberParts.size == 2) {
                        // if double parts contains 2 digits
                        if (numberParts[1].length == 2) {
                            isCorrect = true
                        }
                    }
                }
            } else {
                isCorrect = TextUtils.isDigitsOnly(sum)
            }
        }
        if (!isCorrect) errorMessage = "формат суммы неверный"
        return isCorrect
    }

    private fun changeDateTimeText(s: CharSequence?, separator: Char, maxLength: Int, changeCount: Int): String? {
        val changedText = StringBuilder()
        if (s == null) return null
        if (s.length > maxLength) {
            // max count of symbols is maxLength
            changedText.clear()
            changedText.append(s.substring(0, s.length - 1)).toString()
        } else {
            val separatorStr = s.filterIndexed { index, _ -> (index + 1) % 3 == 0 }
            var stringIsCorrect = true
            for (symbol in separatorStr.iterator()) {
                if (symbol != separator) {
                    stringIsCorrect = false
                    break
                }
            }
            if (!stringIsCorrect) {
                if (changeCount != 0) {
                    val textWithoutSeparator = s.filter { it != separator }
                    changedText.clear()
                    for (index in 0 until textWithoutSeparator.length) {
                        if (index != 0 && index % 2 == 0) changedText.append(separator)
                        changedText.append(textWithoutSeparator[index])
                    }
                } else return null
            } else {
                return null
            }
        }
        return changedText.toString()
    }
}