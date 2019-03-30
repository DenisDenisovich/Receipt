package shiverawe.github.com.receipt.ui.history_v2.month_v2.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import shiverawe.github.com.receipt.entity.receipt.base.Meta
import shiverawe.github.com.receipt.entity.receipt.base.Shop
import shiverawe.github.com.receipt.entity.receipt.month.ReceiptMonth
import shiverawe.github.com.receipt.entity.receipt.month.ReceiptMonth_v2
import shiverawe.github.com.receipt.ui.base.adapter.AdapterDelegatesManager
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*
import kotlin.collections.ArrayList

class MonthAdapter(val shopIsClicked: (receipt: ReceiptMonth_v2) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val delegateManager = AdapterDelegatesManager<ReceiptMonth_v2>()
    private val items: ArrayList<ReceiptMonth_v2> = ArrayList()
    private var calendar = GregorianCalendar(TimeZone.getTimeZone("UTC"))

    init {
        delegateManager.addDelegate(HeaderDateDelegate(0))
        delegateManager.addDelegate(ItemReceiptAdapterDelegate(1))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val holder = delegateManager.onCreateViewHolder(parent, viewType)
        holder.itemView.setOnClickListener {
            val position = holder.adapterPosition
            if (items[position].receiptId != -1L) shopIsClicked(items[position])
        }
        return holder
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        delegateManager.onBindViewHolder(holder, items, position)
    }

    override fun getItemViewType(position: Int): Int {
        return delegateManager.getViewType(items, position) ?: 1488
    }

    fun setItems(receipts: ArrayList<ReceiptMonth_v2>) {
        setTypes(receipts)
        items.clear()
        items.addAll(setTypes(receipts))
        notifyDataSetChanged()
    }

    private fun setTypes(oldList: ArrayList<ReceiptMonth_v2>): ArrayList<ReceiptMonth_v2> {
        val receipts = ArrayList<ReceiptMonth_v2>()
        if (oldList.size > 1) {
            receipts.add(getDateItem(oldList.first().shop.date))
            receipts.add(oldList[1])
            for (index in 1 until oldList.size) {
                if (!isEqualDays(oldList[index - 1].shop.date, oldList[index].shop.date)) {
                    receipts.add(getDateItem(oldList[index].shop.date))
                }
                receipts.add(oldList[index])
            }
        }
        return receipts
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

    private fun clearCalendar() {
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
    }

    private fun getDateItem(date: Long): ReceiptMonth_v2 {
        return ReceiptMonth_v2(-1L, Shop(date, "", ""), Meta("", "", "", "", 0.0))
    }
 }