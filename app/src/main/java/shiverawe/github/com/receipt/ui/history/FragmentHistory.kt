package shiverawe.github.com.receipt.ui.history

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
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
    private val dateFormatter = DateFormat.getDateInstance(SimpleDateFormat.LONG, Locale("ru"))
    private var calendar = GregorianCalendar(TimeZone.getTimeZone("UTC"))
    private lateinit var dateDialog: DatePickerDialog

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
        //btn_history_search.setOnClickListener(this)
        btn_history_calendar.setOnClickListener(this)

        monthAdapter = FragmentPagerAdapter(childFragmentManager)
        vp_history.adapter = monthAdapter
        vp_history.currentItem = monthAdapter.count - 1
        //tab_layout_history.setMonth(Date(monthAdapter.dates[monthAdapter.count - 1]))
        setCurrentYear(monthAdapter.count - 1)

        pageListener.addPageListener(vp_history, object: PageChangeListener.PageListener {
            override fun monthIsSelected() {
                //setMonth()
            }
            override fun monthIsMoved(offset: Float) {
                //tab_layout_history.moveMonth(offset)
            }
        })

       /* tab_layout_history.addMonthClickListener(object : TabLayout.MonthClickListener {
            override fun leftMonthIsClicked() {
                vp_history.currentItem--
            }

            override fun rightMonthIsClicked() {
                vp_history.currentItem++
            }
        })*/
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fab_history_qr -> navigation.openQr()
            R.id.btn_history_navigation -> navigation.openNavigationDrawable()
            //R.id.btn_history_search -> Toast.makeText(context, "search", Toast.LENGTH_SHORT).show()
            R.id.btn_history_calendar -> dateDialog.show()
        }
    }

    private fun setCurrentYear(position: Int) {
        calendar.time = Date(monthAdapter.dates[position])
        val currentMonth = dateFormatter.format(calendar.time).split(" ")[2]
        tv_history_toolbar_title.text = resources.getString(R.string.history_titile) + " $currentMonth"
    }

/*
    fun setMonthSum(date: Int, sum: String) {
        val sumPosition = monthAdapter.getPositionByDate(Date(date.toLong() * 1000))
        if (sumPosition == vp_history.currentItem) ""
           // tab_layout_history.setSum(sum)
    }
*/
/*
    private fun setMonth() {
        val position = vp_history.currentItem
        val date = monthAdapter.dates[position]
        //tab_layout_history.setMonth(Date(date))
        setCurrentYear(position)
        if (position == monthAdapter.count - 1) {
            checkFirstMonthSum(date)
        }
    }*/

/*    private fun checkFirstMonthSum(date: Long) {
        var totalSum: String
        childFragmentManager.fragments.forEach { fragment ->
            if (fragment is MonthFragment) {
                if (fragment.arguments!!.getInt(MonthFragment.DATE_KEY) == (date / 1000).toInt()) {
                    totalSum = fragment.getTotalSum()
                    if (totalSum.isNotBlank()) {
                        setMonthSum((date / 1000).toInt(), totalSum)
                        return@forEach
                    }
                }
            }
        }
    }*/

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