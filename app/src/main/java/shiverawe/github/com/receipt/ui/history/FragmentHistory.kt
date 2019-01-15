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
    private val dateFormatter = DateFormat.getDateInstance(SimpleDateFormat.LONG, Locale("ru"))
    private val dateFormatterMonth = SimpleDateFormat("LLLL", Locale("ru"))
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
        changeTabPosition(monthAdapter.count - 1)

        vp_history.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(p0: Int) {}

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {}

            override fun onPageSelected(position: Int) {
                changeTabPosition(position)
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

    private fun changeTabPosition(position: Int) {
        val date = Date(monthAdapter.dates[position])
        val year = dateFormatter.format(date).split(" ")[2]
        val month = dateFormatterMonth.format(date).capitalize()
        tab_layout_history.setMonth("$month $year")
    }

    fun setMonthSum(sum: String) {
        tab_layout_history.setSum(sum)
    }
}