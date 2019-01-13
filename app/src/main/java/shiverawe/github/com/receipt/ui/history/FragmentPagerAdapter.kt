package shiverawe.github.com.receipt.ui.history

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import shiverawe.github.com.receipt.ui.history.month.MonthFragment
import java.util.*

class FragmentPagerAdapter(fm: FragmentManager): FragmentStatePagerAdapter(fm) {
    val dates: ArrayList<Long> = ArrayList(count)
    init {
        val currentDate = Date(System.currentTimeMillis())
        val calendar = GregorianCalendar()
        calendar.time = currentDate
        calendar.add(Calendar.MONTH, - count / 2)
        for (dateIndex in 0 until count) {
            dates.add(calendar.timeInMillis)
            calendar.add(Calendar.MONTH, 1)
        }
    }

    override fun getItem(position: Int): Fragment {
        val fragment = MonthFragment.getInstance((dates[position] / 1000).toInt())
        return fragment
    }

    override fun getCount() = 100

}