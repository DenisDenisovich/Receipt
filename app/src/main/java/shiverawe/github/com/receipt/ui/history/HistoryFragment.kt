package shiverawe.github.com.receipt.ui.history

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.transition.Fade
import android.transition.TransitionManager
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_history.*
import shiverawe.github.com.receipt.ui.MainActivity
import shiverawe.github.com.receipt.R
import shiverawe.github.com.receipt.ui.Navigation
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class HistoryFragment : Fragment(), View.OnClickListener {

    private lateinit var navigation: Navigation

    private val monthAdapter: FragmentPagerAdapter by lazy {
        FragmentPagerAdapter(childFragmentManager)
    }

    private val dateFormatterYear = DateFormat.getDateInstance(SimpleDateFormat.LONG, Locale("ru"))
    private val dateFormatterMonth = SimpleDateFormat("LLLL", Locale("ru"))
    private var calendar = GregorianCalendar()
    private lateinit var dateDialog: DatePickerDialog

    override fun onAttach(context: Context) {
        super.onAttach(context)
        navigation = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // init date dialog
        dateDialog = DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                val selectedDate = GregorianCalendar().apply {
                    set(Calendar.YEAR, year)
                    set(Calendar.MONTH, month)
                    set(Calendar.DAY_OF_MONTH, dayOfMonth)
                }
                setMonthPage(selectedDate.time)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        btn_calendar_history.setOnClickListener(this)
        btn_next_history.setOnClickListener(this)
        btn_preview_history.setOnClickListener(this)

        vp_history.adapter = monthAdapter
        vp_history.currentItem = monthAdapter.count - 1

        vp_history.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            private var previewItem = vp_history.currentItem
            override fun onPageSelected(position: Int) {
                if (previewItem != position) {
                    previewItem = position
                    updateMonthDate(position)
                }
            }
        })

        updateMonthDate(vp_history.currentItem)
        setSum("")
    }

    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.btn_calendar_history -> {
                dateDialog.show()
            }

            R.id.btn_next_history -> {
                if (vp_history.currentItem != monthAdapter.count - 1) {
                    vp_history.currentItem++
                    updateMonthDate(vp_history.currentItem)
                }
            }

            R.id.btn_preview_history -> {
                if (vp_history.currentItem != 0) {
                    vp_history.currentItem--
                    updateMonthDate(vp_history.currentItem)
                }
            }
        }
    }

    fun setSum(sum: String) {
        TransitionManager.beginDelayedTransition(bottom_menu_history, Fade())
        if (sum.isNotEmpty()) {

            tv_sum_history.text = getString(R.string.total_sum, sum)
        } else {
            tv_sum_history.text = getString(R.string.empty_total_sum)
        }
    }

    @SuppressLint("SetTextI18n", "DefaultLocale")
    private fun updateMonthDate(position: Int) {
        calendar.time = Date(monthAdapter.dates[position])
        val month = dateFormatterMonth.format(calendar.time).capitalize()
        val year = dateFormatterYear.format(calendar.time).split(" ")[2]
        tv_date_current_history.text = "$month $year"
    }

    private fun setMonthPage(date: Date) {
        val position = monthAdapter.getPositionByDate(date)
        if (position == -1) {
            Toast.makeText(context, getString(R.string.error_set_month), Toast.LENGTH_SHORT).show()
        } else {
            vp_history.currentItem = position
        }
    }

    companion object {
        const val HISTORY_TAG = "receipt_history"
    }
}
