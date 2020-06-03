package shiverawe.github.com.receipt.ui.receipt.create

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_manual.*
import org.koin.androidx.viewmodel.ext.android.getSharedViewModel
import shiverawe.github.com.receipt.R
import shiverawe.github.com.receipt.domain.entity.base.Meta
import shiverawe.github.com.receipt.utils.toLongWithMilliseconds

class ManualFragment : CreateReceiptFragment(R.layout.fragment_manual), View.OnFocusChangeListener {

    private val viewMode: CreateReceiptViewModel by lazy {
        getSharedViewModel<CreateReceiptViewModel>(from = { requireParentFragment() })
    }
    private val stateObserver = Observer<CreateReceiptUiState> { state ->
        if (state is ManualState) {
            when {
                state.isWaiting -> {
                    showDialog()
                }

                !state.isWaiting && state.error == null -> {
                    dismissDialog()
                }

                state.error != null -> {
                    state.error?.let { errorState ->
                        showError(errorState)
                        viewMode.onShowError()
                    }
                }
            }
        }
    }

    private var meta: Meta? = null
    private var textIsValid = false
    private var errorMessage: String = ""

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
                et_manual_date?.setText(newText)
                et_manual_date?.setSelection(newText.length)
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
                et_manual_time?.setText(newText)
                et_manual_time?.setSelection(newText.length)
            }
        }
    }

    private fun createPopupWindow(btn_info: View) {
        val window = PopupWindow(activity)
        val view = layoutInflater.inflate(R.layout.popup_info_text, null)
        window.contentView = view
        window.isFocusable = true
        val textView = view.findViewById<TextView>(R.id.pp_info_tv)
        when (btn_info.id) {
            R.id.btn_info_fn -> {
                textView.text = getString(R.string.info_pp_fn)
            }
            R.id.btn_info_fd -> {
                textView.text = getString(R.string.info_pp_fd)
            }
            R.id.btn_info_fp -> {
                textView.text = getString(R.string.info_pp_fp)
            }
        }
        textView.setOnClickListener {
            window.dismiss()
        }
        window.setBackgroundDrawable(null)
        window.showAsDropDown(btn_info, (-0.5 * btn_info.width).toInt(), 0)
    }

    private fun createImagePopupWindow(btn_info: View):Boolean {
        val window = PopupWindow(activity)
        val view = layoutInflater.inflate(R.layout.popup_info_image, null)
        window.contentView = view
        window.isFocusable = true
        val imageView = view.findViewById<ImageView>(R.id.info_image)
        when (btn_info.id) {
            R.id.btn_info_fn -> {
                imageView.setImageResource(R.drawable.fn_info_img)
            }
            R.id.btn_info_fd -> {
                imageView.setImageResource(R.drawable.fd_info_img)
            }
            R.id.btn_info_fp -> {
                imageView.setImageResource(R.drawable.fp_info_img)
            }
        }
        val windowView = view.findViewById<LinearLayout>(R.id.popup_image)
        windowView.setOnClickListener {
            window.dismiss()
        }
        window.setBackgroundDrawable(null)
        window.showAtLocation(btn_info, Gravity.CENTER_HORIZONTAL, 0, 0)
        return true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        changeBtnBackground()
        btn_manual?.setOnClickListener {
            createReceipt()
        }

        btn_info_fn.setOnClickListener { createPopupWindow(it) }
        btn_info_fn.setOnLongClickListener { createImagePopupWindow(it) }

        btn_info_fd.setOnClickListener { createPopupWindow(it) }
        btn_info_fd.setOnLongClickListener { createImagePopupWindow(it) }

        btn_info_fp.setOnClickListener { createPopupWindow(it) }
        btn_info_fp.setOnLongClickListener { createImagePopupWindow(it) }


        btn_manual_back?.setOnClickListener {
            viewMode.goBack()
        }

        et_manual_fd?.onFocusChangeListener = this
        et_manual_fn?.onFocusChangeListener = this
        et_manual_fp?.onFocusChangeListener = this
        et_manual_sum?.onFocusChangeListener = this
        et_manual_date?.onFocusChangeListener = this
        et_manual_time?.onFocusChangeListener = this
        et_manual_fd?.addTextChangedListener(textWatcher)
        et_manual_fn?.addTextChangedListener(textWatcher)
        et_manual_fp?.addTextChangedListener(textWatcher)
        et_manual_sum?.addTextChangedListener(textWatcher)
        et_manual_date?.addTextChangedListener(dateTextWatcher)
        et_manual_time?.addTextChangedListener(timeTextWatcher)

        et_manual_time.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                createReceipt()
            }
            false
        }
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        changeEtBackground(et_manual_fd)
        changeEtBackground(et_manual_fn)
        changeEtBackground(et_manual_fp)
        changeEtBackground(et_manual_sum)
        changeEtBackground(et_manual_date)
        changeEtBackground(et_manual_time)
        changeBtnBackground()
    }

    override fun onCancelDialogClick() {
        viewMode.onCancelWaiting()
    }

    override fun onResume() {
        super.onResume()
        viewMode.state.observe(this, stateObserver)
    }

    override fun onPause() {
        super.onPause()
        viewMode.state.removeObserver(stateObserver)
    }

    private fun createReceipt() {
        if (textIsValid) {
            meta?.let { viewMode.createReceipt(it) }
        } else {
            viewMode.showError(errorMessage)
        }
    }

    private fun changeEtBackground(view: EditText?) {
        if (view == null) return
        if (!view.hasFocus() && view.text.toString().isNotEmpty())
            view.setBackgroundResource(R.drawable.et_manual_finish)
        else
            view.setBackgroundResource(R.drawable.et_manual)
    }

    private fun changeBtnBackground() {
        if (checkData()) {
            btn_manual?.setBackgroundResource(R.drawable.btn_blue)
        } else {
            btn_manual?.setBackgroundResource(R.drawable.btn_gray)
        }
    }

    private fun checkData(): Boolean {
        val fd = et_manual_fd?.text.toString().trim()
        val fn = et_manual_fn?.text.toString().trim()
        val fp = et_manual_fp?.text.toString().trim()
        val s = et_manual_sum?.text.toString().trim()
        val date = et_manual_date?.text.toString().trim()
        val time = et_manual_time?.text.toString().trim()
        if (checkFd(fd) &&
                checkFn(fn) &&
                checkFp(fp) &&
                checkSum(s) &&
                checkDate(date) &&
                checkTime(time)
        ) {
            val t = getDateString(date, time)
            meta = Meta(t.toLongWithMilliseconds(), fn, fd, fp, s.toDouble())
            textIsValid = true
        } else {
            textIsValid = false
        }
        return textIsValid
    }

    // 2019-06-07T22:54:00
    private fun getDateString(date: String, time: String): String {
        val dateValues = date.split(".")
        val timeValues = time.split(":")
        val year = "20${dateValues.last()}"
        val month = if (dateValues[1].length == 1) "0${dateValues[1]}" else dateValues[1]
        val day = if (dateValues[0].length == 1) "0${dateValues[0]}" else dateValues[0]
        val hour = if (timeValues[0].length == 1) "0${timeValues[0]}" else timeValues[0]
        val minute = if (timeValues[1].length == 1) "0${timeValues[1]}" else timeValues[1]
        val t = StringBuilder()
        t.append("$year-")
        t.append("$month-")
        t.append(day)
        t.append("T")
        t.append("$hour:")
        t.append("$minute:")
        t.append("00")
        return t.toString()
    }

    private fun checkFd(fd: String): Boolean {
        val isCorrect = fd.isNotEmpty()
        if (!isCorrect) errorMessage = getString(R.string.fd_error)
        return isCorrect
    }

    private fun checkFn(fn: String): Boolean {
        val isCorrect = fn.isNotEmpty()
        if (!isCorrect) errorMessage = getString(R.string.fn_error)
        return isCorrect
    }

    private fun checkFp(fp: String): Boolean {
        val isCorrect = fp.isNotEmpty()
        if (!isCorrect) errorMessage = getString(R.string.fp_error)
        return isCorrect
    }

    private fun checkDate(date: String): Boolean {
        var isCorrect = false
        val values = date.split(".")
        if (values.size == 3) {
            isCorrect = try {
                val day = values[0].toInt()
                val month = values[1].toInt()
                val year = values[2].toInt()
                month in 0..12 && day in 0..31 && year in 0..99
            } catch (e: Exception) {
                false
            }
        }
        if (!isCorrect) errorMessage = getString(R.string.date_error)
        return isCorrect
    }

    private fun checkTime(time: String): Boolean {
        var isCorrect = false
        val values = time.split(":")
        if (values.size == 2) {
            isCorrect = try {
                val hour = values[0].toInt()
                val minute = values[1].toInt()
                hour in 0..23 && minute in 0..59
            } catch (e: Exception) {
                false
            }
        }
        if (!isCorrect) errorMessage = getString(R.string.time_error)
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
        if (!isCorrect) errorMessage = getString(R.string.sum_error)
        return isCorrect
    }

    private fun changeDateTimeText(
            s: CharSequence?,
            separator: Char,
            maxLength: Int,
            changeCount: Int
    ): String? {
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
                    for (index in textWithoutSeparator.indices) {
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