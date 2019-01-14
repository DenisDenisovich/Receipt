package shiverawe.github.com.receipt.ui.history.month

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_month.view.*
import shiverawe.github.com.receipt.R
import shiverawe.github.com.receipt.data.Receipt
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class RvAdapterMonth(val receipts: ArrayList<Receipt?>, val shopIsClicked: (receipt: Receipt) -> Unit): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val SHOP_VIEW_TYPE = 0
    private val SEPARATOR_VIEW_TYPE = 1
    private val dateFormatter = DateFormat.getDateInstance(SimpleDateFormat.FULL, Locale("ru"))
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val holder: RecyclerView.ViewHolder
        if (viewType == SHOP_VIEW_TYPE) {
            holder = ShopViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_month, parent, false))
            holder.itemView.setOnClickListener { shopIsClicked(receipts[holder.adapterPosition]!!) }
        } else
            holder = SeparatorViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.separator_month, parent, false))
        return holder
    }

    override fun getItemCount() = receipts.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ShopViewHolder) {
            val date = dateFormatter.format(Date(receipts[position]!!.shop.date)).split(",")
            val dayStrLength = if (7 <= date[0].length) 7 else date[0].length
            holder.day.text = date[0].substring(0, dayStrLength)
            holder.dayDigit.text = date[1].split(" ")[1]
            holder.name.text = receipts[position]!!.shop.place
            holder.sum.text = receipts[position]!!.shop.sum
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (receipts[position] == null) SEPARATOR_VIEW_TYPE else SHOP_VIEW_TYPE
    }

    class ShopViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val dayDigit = view.tv_item_month_day_digit
        val day = view.tv_item_month_day
        val name = view.tv_item_month_name
        val sum = view.tv_item_month_sum
    }
    class SeparatorViewHolder(view: View): RecyclerView.ViewHolder(view)
}