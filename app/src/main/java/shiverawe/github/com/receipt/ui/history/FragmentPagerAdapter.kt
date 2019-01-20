package shiverawe.github.com.receipt.ui.history

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import shiverawe.github.com.receipt.ui.history.month.MonthFragment
import java.util.*

class FragmentPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
    val dates: ArrayList<Long> = ArrayList(MutableList(count) { 0L })

    init {
        // init dates
        val calendar = GregorianCalendar(TimeZone.getDefault())
        calendar.time = Date(System.currentTimeMillis())
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.clear(Calendar.HOUR)
        calendar.clear(Calendar.MINUTE)
        calendar.clear(Calendar.SECOND)
        calendar.clear(Calendar.MILLISECOND)
        for (dateIndex in dates.size - 1 downTo 0) {
            dates[dateIndex] = calendar.timeInMillis
            calendar.add(Calendar.MONTH, -1)
        }
    }

    override fun getItem(position: Int): Fragment {
        return MonthFragment.getInstance((dates[position] / 1000).toInt())
    }

    override fun getCount() = 1200

    fun getPositionByDate(date: Date): Int {
        val calendar = GregorianCalendar(TimeZone.getDefault())
        calendar.time = date
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.clear(Calendar.HOUR)
        calendar.clear(Calendar.MINUTE)
        calendar.clear(Calendar.SECOND)
        calendar.clear(Calendar.MILLISECOND)
        return dates.indexOf(calendar.timeInMillis)
    }

}