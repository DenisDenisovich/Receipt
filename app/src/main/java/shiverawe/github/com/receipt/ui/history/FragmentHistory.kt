package shiverawe.github.com.receipt.ui.history

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
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class FragmentHistory: Fragment(), View.OnClickListener {
    lateinit var navigation: Navigation
    lateinit var monthAdapter: FragmentPagerAdapter
    private val dateFormatter = DateFormat.getDateInstance(SimpleDateFormat.LONG, Locale("ru"))
    var currentMonth = ""
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
                if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                    if (previewState != ViewPager.SCROLL_STATE_IDLE) {
                        // when scroll is very fast
                        // end of animation. Update month array in TabLayout
                        val position = vp_history.currentItem
                        tab_layout_history.setMonth(Date(monthAdapter.dates[position]))
                        setCurrentYear(position)
                        offset = 0F
                    }
                }
                if (state == ViewPager.SCROLL_STATE_DRAGGING) pageIsSelected = false
                previewState = state
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, p2: Int) {
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
                    tab_layout_history.setMonth(Date(monthAdapter.dates[position]))
                    setCurrentYear(position)
                    offset = 0F
                }
            }

            override fun onPageSelected(position: Int) {
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
                Toast.makeText(context, "Calendar", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun setMonthSum(sum: String) {
        tab_layout_history.setSum(sum)
    }
}