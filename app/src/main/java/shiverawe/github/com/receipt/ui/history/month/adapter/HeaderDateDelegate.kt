package shiverawe.github.com.receipt.ui.history.month.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.header_receipt.view.*
import shiverawe.github.com.receipt.R
import shiverawe.github.com.receipt.domain.entity.receipt.month.ReceiptMonth
import shiverawe.github.com.receipt.ui.base.adapter.AdapterDelegate
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class HeaderDateDelegate(override var viewType: Int) : AdapterDelegate<ReceiptMonth> {
    private val dateFormatterDate = SimpleDateFormat("dd.MM_HH:mm", Locale("ru"))
    private val dateFormatterDay = DateFormat.getDateInstance(SimpleDateFormat.FULL, Locale("ru"))
    private var calendar = GregorianCalendar(TimeZone.getTimeZone("UTC"))

    init {
        dateFormatterDate.timeZone = TimeZone.getTimeZone("UTC")
        dateFormatterDay.timeZone = TimeZone.getTimeZone("UTC")
    }

    override fun isForViewType(items: ArrayList<ReceiptMonth>, position: Int): Boolean {
        return items[position].receiptId == -1L
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return HeaderDateViewHolder(getLayout(parent, R.layout.header_receipt))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, items: ArrayList<ReceiptMonth>, position: Int) {
        (holder as HeaderDateViewHolder).bind(items[position])
    }

    inner class HeaderDateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvDate = itemView.tv_header_receipt
        @SuppressLint("SetTextI18n")
        fun bind(receipt: ReceiptMonth) {
            when {
                isEqualDays(receipt.shop.date, System.currentTimeMillis()) -> tvDate.text = "СЕГОДНЯ"
                isPreviewDay(receipt.shop.date) -> tvDate.text = "ВЧЕРА"
                else -> {
                    val day = dateFormatterDay.format(Date(receipt.shop.date)).split(",")[0].toUpperCase()
                    val digit = dateFormatterDate.format(Date(receipt.shop.date)).split("_")[0].split(".")[0]
                    tvDate.text = "$digit $day"
                }
            }
        }

        private fun isEqualDays(firstDay: Long, secondDay: Long): Boolean {
            calendar.timeInMillis = firstDay
            clearCalendar()
            val second = calendar.timeInMillis
            calendar.timeInMillis = secondDay
            clearCalendar()
            val next = calendar.timeInMillis
            return second == next
        }

        private fun isPreviewDay(date: Long): Boolean {
            val today = System.currentTimeMillis()
            calendar.timeInMillis = today
            clearCalendar()
            calendar.add(Calendar.DAY_OF_MONTH, -1)
            return isEqualDays(calendar.timeInMillis, date)
        }

        private fun clearCalendar() {
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
        }
    }
}