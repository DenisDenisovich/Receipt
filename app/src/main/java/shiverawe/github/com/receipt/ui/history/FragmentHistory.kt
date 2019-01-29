package shiverawe.github.com.receipt.ui.history

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.util.Log
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

class FragmentHistory: Fragment(), View.OnClickListener {
    lateinit var navigation: Navigation
    lateinit var monthAdapter: FragmentPagerAdapter
    private val dateFormatter = DateFormat.getDateInstance(SimpleDateFormat.LONG, Locale("ru"))
    var currentMonth = ""
    var changeDateByCalendar = false
    var calendar = GregorianCalendar()
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        navigation = context as MainActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fab_history_qr.setOnClickListener(this)
        btn_history_navigation.setOnClickListener(this)
        btn_history_search.setOnClickListener(this)
        btn_history_calendar.setOnClickListener(this)
        monthAdapter = FragmentPagerAdapter(childFragmentManager)
        vp_history.adapter = monthAdapter
        vp_history.currentItem = monthAdapter.count - 1
        tab_layout_history.setMonth(Date(monthAdapter.dates[monthAdapter.count - 1]))
        setCurrentYear(monthAdapter.count - 1)

        vp_history.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            var moveToRight = false
            var previewPosition = vp_history.currentItem
            var offset = 0F
            var pageIsSelected = false
            var previewState = 0
            override fun onPageScrollStateChanged(state: Int) {
                Log.d("LogState", state.toString())
                if (changeDateByCalendar) {
                    if (state == ViewPager.SCROLL_STATE_SETTLING) {
                        setMonth()
                        offset = 0F
                    } else if (state == ViewPager.SCROLL_STATE_IDLE) {
                        changeDateByCalendar = false
                    }
                } else if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                    pageIsSelected = false
                    if (previewState != ViewPager.SCROLL_STATE_IDLE) {
                        // when scroll is very fast
                        // end of animation. Update month array in TabLayout
                        setMonth()
                        offset = 0F
                    }
                }
                previewState = state
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, p2: Int) {
                if (changeDateByCalendar) return
                if (!pageIsSelected) {
                    moveToRight = position < vp_history.currentItem
                    previewPosition = position
                }
                if (positionOffset != 0F) {
                    // animation in progress. Move month text
                    offset = if (moveToRight)
                        1 - positionOffset
                    else
                        - positionOffset
                    tab_layout_history.moveMonth(offset)
                } else {
                    // end of animation. Update month array in TabLayout
                    setMonth()
                    offset = 0F
                }
            }

            override fun onPageSelected(position: Int) {
                if (changeDateByCalendar) return
                pageIsSelected = true
                if (offset == 0F) {
                    // if page selected by currentItem without swipe
                    moveToRight = previewPosition > position
                    previewPosition = position
                }
            }
        })

        tab_layout_history.subscribeToClickListener(object: TabLayout.MonthClickListener {
            override fun leftMonthIsClicked() {
                vp_history.currentItem--
            }
            override fun rightMonthIsClicked() {
                vp_history.currentItem++
            }
        })
    }

    private fun setCurrentYear(position: Int) {
        calendar.time = Date(monthAdapter.dates[position])
        currentMonth = dateFormatter.format(calendar.time).split(" ")[2]
        tv_history_toolbar_title.text = resources.getString(R.string.history_titile) + " $currentMonth"
    }
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fab_history_qr -> {
                navigation.openQr()
            }

            R.id.btn_history_navigation -> {
                navigation.openNavigationDrawable()
            }

            R.id.btn_history_search -> {
                Toast.makeText(context, "search", Toast.LENGTH_SHORT).show()
            }

            R.id.btn_history_calendar -> {
                openDateDialog()
            }
        }
    }

    fun setMonthSum(sum: String) {
        tab_layout_history.setSum(sum)
    }

    fun setMonth() {
        val position = vp_history.currentItem
        tab_layout_history.setMonth(Date(monthAdapter.dates[position]))
        setCurrentYear(position)
    }
    private fun openDateDialog() {
        val currentDate = GregorianCalendar()
        currentDate.time = Date(System.currentTimeMillis())
        val dateDialog = DatePickerDialog(context, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            val selectedDate = GregorianCalendar(TimeZone.getDefault())
            selectedDate.set(Calendar.YEAR, year)
            selectedDate.set(Calendar.MONTH, month)
            selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            setNewDate(selectedDate.time)
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH),  currentDate.get(Calendar.DAY_OF_MONTH))
        dateDialog.show()
    }

    private fun setNewDate(date: Date) {
        val position = monthAdapter.getPositionByDate(date)
        if(position == -1)  {
            Toast.makeText(context, "Невозможно отобразить данный месяц", Toast.LENGTH_SHORT).show()
        } else {
            changeDateByCalendar = true
            vp_history.currentItem = position
        }
    }

    fun updateMonth(date: Long) {
        val updatedPosition = monthAdapter.getPositionByDate(Date(date))
        if (updatedPosition != -1 && Math.abs(updatedPosition - vp_history.currentItem) <= 1) {
            val fragments = childFragmentManager.fragments
            fragments.forEach {
                fragment ->
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