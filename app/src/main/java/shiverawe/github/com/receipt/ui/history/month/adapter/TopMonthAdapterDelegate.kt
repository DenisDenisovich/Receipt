package shiverawe.github.com.receipt.ui.history.month.adapter

import android.graphics.Outline
import android.graphics.Path
import android.graphics.Rect
import android.graphics.RectF
import android.os.Build
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.item_month_top.view.*
import shiverawe.github.com.receipt.R
import shiverawe.github.com.receipt.entity.receipt.month.ReceiptMonth
import shiverawe.github.com.receipt.ui.App
import shiverawe.github.com.receipt.ui.base.adapter.AdapterDelegate
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class TopMonthAdapterDelegate(override var viewType: Int) : AdapterDelegate<ReceiptMonth> {
    private val dateFormatter = DateFormat.getDateInstance(SimpleDateFormat.FULL, Locale("ru"))

    init {
        dateFormatter.timeZone = TimeZone.getTimeZone("UTC")
    }

    override fun isForViewType(items: ArrayList<ReceiptMonth>, position: Int): Boolean {
        return items[position].viewType == viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return TopMonthViewHolder(getLayout(parent, R.layout.item_month_top))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, items: ArrayList<ReceiptMonth>, position: Int) {
        (holder as TopMonthViewHolder).bind(items[position])
    }

    inner class TopMonthViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dayDigit = itemView.tv_item_month_day_digit
        private val day = itemView.tv_item_month_day
        private val name = itemView.tv_item_month_name
        private val sum = itemView.tv_item_month_sum
        private val separator = itemView.separator

        fun bind(receipt: ReceiptMonth) {
            val date = dateFormatter.format(Date(receipt.shop.date)).split(", ")
            day.text = if (date[0].length > 7) date[0].substring(0, 7) else date[0]
            dayDigit.text = date[1].substring(0, date[1].indexOf(' '))
            name.text = receipt.shop.place
            sum.text = receipt.shop.sum
            if (receipt.separatorIsVisible) separator.visibility = View.VISIBLE
            else separator.visibility = View.GONE
            if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                setOutlineProvider()
            }
        }

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        private fun setOutlineProvider() {
            val outlineProvider =
            object : ViewOutlineProvider() {
                override fun getOutline(view: View?, outline: Outline?) {
                    val bottomOffset = App.appContext.resources.getDimensionPixelSize(R.dimen.item_month_elevation)
                    val radiusOffset = App.appContext.resources.getDimension(R.dimen.item_month_radius)
                    outline?.setRoundRect(0,0,view!!.width, view.height - bottomOffset, radiusOffset)
                }
            }
            itemView.outlineProvider = outlineProvider
        }
    }
}