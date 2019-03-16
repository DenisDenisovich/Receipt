package shiverawe.github.com.receipt.ui.history.month.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import shiverawe.github.com.receipt.R
import shiverawe.github.com.receipt.entity.receipt.base.Meta
import shiverawe.github.com.receipt.entity.receipt.base.Shop
import shiverawe.github.com.receipt.entity.receipt.month.ReceiptMonth
import shiverawe.github.com.receipt.ui.App
import shiverawe.github.com.receipt.ui.base.adapter.AdapterDelegatesManager
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*
import kotlin.collections.ArrayList

private const val SUM_VIEW_TYPE = 0
private const val SHOP_SINGLE_VIEW_TYPE = 1
private const val SHOP_TOP_VIEW_TYPE = 2
private const val SHOP_MIDDLE_VIEW_TYPE = 3
private const val SHOP_BOTTOM_VIEW_TYPE = 4
private const val BOTTOM_SEPARATOR_VIEW_TYPE = 5

class MonthAdapter(val shopIsClicked: (receipt: ReceiptMonth) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val delegateManager = AdapterDelegatesManager<ReceiptMonth>()
    private val items: ArrayList<ReceiptMonth> = ArrayList()

    private var calendar = GregorianCalendar(TimeZone.getTimeZone("UTC"))
    private var totalSum = ""

    init {
        delegateManager.addDelegate(SumMonthAdapterDelegate(SUM_VIEW_TYPE))
        delegateManager.addDelegate(SingleMonthAdapterDelegate(SHOP_SINGLE_VIEW_TYPE))
        delegateManager.addDelegate(TopMonthAdapterDelegate(SHOP_TOP_VIEW_TYPE))
        delegateManager.addDelegate(MiddleMonthAdapterDelegate(SHOP_MIDDLE_VIEW_TYPE))
        delegateManager.addDelegate(BottomMonthAdapterDelegate(SHOP_BOTTOM_VIEW_TYPE))
        delegateManager.addDelegate(BottomSeparatorMonthAdapterDelegate(BOTTOM_SEPARATOR_VIEW_TYPE))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val holder = delegateManager.onCreateViewHolder(parent, viewType)
        holder.itemView.setOnClickListener {
            val position = holder.adapterPosition
            if (items[position].viewType != SUM_VIEW_TYPE)
                shopIsClicked(items[position])
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

    fun setItems(receipts: ArrayList<ReceiptMonth?>) {
        setTypes(receipts)
        notifyDataSetChanged()
    }

    private fun setTypes(receipts: ArrayList<ReceiptMonth?>) {
        setSum(receipts)
        for (index in 1 until receipts.size - 1) {
            val preview = receipts[index - 1]
            val next = receipts[index + 1]
            val current = receipts[index]
            current?.let {
                when {
                    preview == null && next == null -> {
                        current.viewType = SHOP_SINGLE_VIEW_TYPE
                        current.separatorIsVisible = false
                    }
                    preview == null && next != null -> {
                        current.viewType = SHOP_TOP_VIEW_TYPE
                        current.separatorIsVisible = !isEqualDays(current.shop.date, next.shop.date)
                        current.isTopInDay = true
                    }
                    preview != null && next != null -> {
                        current.viewType = SHOP_MIDDLE_VIEW_TYPE
                        current.separatorIsVisible = !isEqualDays(current.shop.date, next.shop.date)
                        if (isEqualDays(current.shop.date, next.shop.date) || isEqualDays(current.shop.date, preview.shop.date)) {
                            current.isTopInDay = !isEqualDays(current.shop.date, preview.shop.date)
                        }
                    }
                    preview != null && next == null -> {
                        current.viewType = SHOP_BOTTOM_VIEW_TYPE
                        current.separatorIsVisible = false
                        current.isTopInDay = !isEqualDays(current.shop.date, preview.shop.date)
                    }
                }
            }
        }
        receipts.last()?.let {
            if (receipts[receipts.lastIndex - 1] == null) receipts.last()!!.viewType = SHOP_SINGLE_VIEW_TYPE
            else {
                receipts.last()!!.viewType = SHOP_BOTTOM_VIEW_TYPE
                val currentDate = receipts.last()!!.shop.date
                val previewDate = receipts[receipts.lastIndex - 1]!!.shop.date
                receipts.last()!!.isTopInDay = !isEqualDays(currentDate, previewDate)
            }
            receipts.last()!!.separatorIsVisible = false
        }
        receipts.add(ReceiptMonth(0, Shop(0L, "", ""), Meta("", "", "", "", 0.0), BOTTOM_SEPARATOR_VIEW_TYPE, true))
        items.clear()
        items.addAll(java.util.ArrayList<ReceiptMonth>(receipts.filter { it != null }))
    }

    private fun setSum(receipts: ArrayList<ReceiptMonth?>) {
        var sum = 0.0
        receipts.forEach { receipt ->
            sum += receipt?.meta?.s ?: 0.0
        }
        totalSum = BigDecimal(sum).setScale(2, RoundingMode.DOWN).toString() + " " + App.appContext.resources.getString(R.string.rubleSymbolJava)
        receipts.add(0, ReceiptMonth(0, Shop(0L, "", totalSum), Meta("", "", "", "", 0.0), SUM_VIEW_TYPE, true))
        receipts.add(1, null)
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
}