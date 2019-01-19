package shiverawe.github.com.receipt.ui.history

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewPropertyAnimator
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_history.*
import shiverawe.github.com.receipt.ui.MainActivity
import shiverawe.github.com.receipt.R
import shiverawe.github.com.receipt.ui.Navigation
import java.util.*

class FragmentHistory: Fragment(), View.OnClickListener {
    lateinit var navigation: Navigation
    lateinit var monthAdapter: FragmentPagerAdapter
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
        tv_history_toolbar_title.text = resources.getString(R.string.history_titile)
        monthAdapter = FragmentPagerAdapter(childFragmentManager)
        vp_history.adapter = monthAdapter
        vp_history.currentItem = monthAdapter.count - 1
        tab_layout_history.setMonth(Date(monthAdapter.dates[monthAdapter.count - 1]))


        vp_history.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            var moveToRight = false
            var previewPosition = -1
            var offset = 0F
            var pageIsSelected = false
            override fun onPageScrollStateChanged(state: Int) {
                if (state == ViewPager.SCROLL_STATE_DRAGGING) pageIsSelected = false
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, p2: Int) {
                if (pageIsSelected) return
                previewPosition = position
                moveToRight = position < vp_history.currentItem
                offset = if (moveToRight)
                    1 - positionOffset
                else
                    - positionOffset
                tab_layout_history.moveMonth(offset)
            }

            override fun onPageSelected(position: Int) {
                pageIsSelected = true
                val newOffset = if (moveToRight) 1F else -1F
                tab_layout_history.startEndAnimation(offset, newOffset, moveToRight)
                moveToRight = false
                previewPosition = -1
                offset = 0F
            }
        })
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