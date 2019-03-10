package shiverawe.github.com.receipt.ui.history

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_history.*
import shiverawe.github.com.receipt.ui.MainActivity
import shiverawe.github.com.receipt.R
import shiverawe.github.com.receipt.ui.Navigation
import shiverawe.github.com.receipt.ui.history.month.MonthFragment
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class FragmentHistory : Fragment(), View.OnClickListener {
    private lateinit var navigation: Navigation
    private lateinit var monthAdapter: FragmentPagerAdapter
    private val pageListener = PageChangeListener()
    private val dateFormatterYaer = DateFormat.getDateInstance(SimpleDateFormat.LONG, Locale("ru"))
    private val dateFormatterMonth = SimpleDateFormat("LLLL", Locale("ru"))
    private var calendar = GregorianCalendar(TimeZone.getTimeZone("UTC"))
    private lateinit var dateDialog: DatePickerDialog

    init {
        dateFormatterMonth.timeZone = TimeZone.getTimeZone("UTC")
    }
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        navigation = context as MainActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // init date dialog
        val currentDate = GregorianCalendar()
        currentDate.time = Date(System.currentTimeMillis())
        dateDialog = DatePickerDialog(context!!, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            val selectedDate = GregorianCalendar(TimeZone.getTimeZone("UTC"))
            selectedDate.apply {
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, month)
                set(Calendar.DAY_OF_MONTH, dayOfMonth)
            }
            setDateFromCalendarDialog(selectedDate.time)
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH))

        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fab_history_qr.setOnClickListener(this)
        btn_history_navigation.setOnClickListener(this)
        btn_history_calendar.setOnClickListener(this)

        monthAdapter = FragmentPagerAdapter(childFragmentManager)
        vp_history.adapter = monthAdapter
        vp_history.currentItem = monthAdapter.count - 1
        setCurrentMonth(monthAdapter.count - 1)

        vp_history.addOnPageChangeListener(object: ViewPager.SimpleOnPageChangeListener(){
            override fun onPageSelected(position: Int) {
                setCurrentMonth(position)
            }
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fab_history_qr -> navigation.openQr()
            R.id.btn_history_navigation -> navigation.openNavigationDrawable()
            R.id.btn_history_calendar -> dateDialog.show()
        }
    }

    private fun setCurrentMonth(position: Int) {
        calendar.time = Date(monthAdapter.dates[position])
        tv_history_toolbar_year.text = dateFormatterYaer.format(calendar.time).split(" ")[2]
        tv_history_toolbar_month.text = dateFormatterMonth.format(calendar.time).capitalize()
    }

    private fun setDateFromCalendarDialog(date: Date) {
        val position = monthAdapter.getPositionByDate(date)
        if (position == -1) {
            Toast.makeText(context, "Невозможно отобразить данный месяц", Toast.LENGTH_SHORT).show()
        } else {
            pageListener.dateChangedByCalendar()
            vp_history.currentItem = position
        }
    }

    fun updateMonth(date: Long) {
        val updatedPosition = monthAdapter.getPositionByDate(Date(date))
        if (updatedPosition != -1 && Math.abs(updatedPosition - vp_history.currentItem) <= 1) {
            val fragments = childFragmentManager.fragments
            fragments.forEach { fragment ->
                if (fragment is MonthFragment) {
                    calendar.timeInMillis = date
                    monthAdapter.setBeginOfMonth(calendar)
                    if (fragment.arguments?.getInt(MonthFragment.DATE_KEY) == (calendar.timeInMillis / 1000).toInt()) {
                        fragment.update()
                        return
                    }
                }
            }
        }
    }
}