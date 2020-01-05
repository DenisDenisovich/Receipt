package shiverawe.github.com.receipt.ui.history

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import shiverawe.github.com.receipt.ui.history.month.MonthFragment
import java.util.*

class FragmentPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
    val dates: ArrayList<Long> = ArrayList(MutableList(count) { 0L })

    init {
        // init dates
        val calendar = GregorianCalendar(TimeZone.getTimeZone("UTC"))
        calendar.time = Date(System.currentTimeMillis())
        for (dateIndex in dates.size - 1 downTo 0) {
            setBeginOfMonth(calendar)
            dates[dateIndex] = calendar.timeInMillis
            calendar.set(Calendar.DAY_OF_MONTH, 15)
            calendar.add(Calendar.MONTH, -1)
        }
    }

    override fun getItem(position: Int): Fragment {
        return MonthFragment.getNewInstance((dates[position] / 1000).toInt(), position)
    }

    override fun getCount() = 1200

    fun getPositionByDate(date: Date): Int {
        val calendar = GregorianCalendar(TimeZone.getTimeZone("UTC"))
        calendar.time = date
        setBeginOfMonth(calendar)
        return dates.indexOf(calendar.timeInMillis)
    }

    fun setBeginOfMonth(calendar: GregorianCalendar) {
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
    }
}