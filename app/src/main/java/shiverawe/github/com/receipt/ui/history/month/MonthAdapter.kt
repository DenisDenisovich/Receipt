package shiverawe.github.com.receipt.ui.history.month

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_month.view.*
import kotlinx.android.synthetic.main.item_sum.view.*
import shiverawe.github.com.receipt.R
import shiverawe.github.com.receipt.entity.Receipt
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MonthAdapter(val shopIsClicked: (receipt: Receipt) -> Unit): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val receipts: ArrayList<Receipt?> = ArrayList()
    private val SHOP_VIEW_TYPE = 0
    private val SEPARATOR_VIEW_TYPE = 1
    private val dateFormatter = DateFormat.getDateInstance(SimpleDateFormat.FULL, Locale("ru"))

    init {
        dateFormatter.timeZone = TimeZone.getTimeZone("UTC")
    }

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
        if (holder is ShopViewHolder) holder.bind(receipts[position]!!)
    }

    override fun getItemViewType(position: Int): Int {
        return if (receipts[position] == null) SEPARATOR_VIEW_TYPE else SHOP_VIEW_TYPE
    }

    fun setItems(receipts: ArrayList<Receipt?>) {
        this.receipts.clear()
        this.receipts.addAll(receipts)
        notifyDataSetChanged()
    }

    inner class ShopViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val dayDigit = view.tv_item_month_day_digit
        private val day = view.tv_item_month_day
        private val name = view.tv_item_month_name
        private val sum = view.tv_item_month_sum

        fun bind(receipt: Receipt) {
            val date = dateFormatter.format(Date(receipt.shop.date)).split(",")
            val dayStrLength = if (7 <= date[0].length) 7 else date[0].length
            day.text = date[0].substring(0, dayStrLength)
            dayDigit.text = date[1].split(" ")[1]
            name.text = receipt.shop.place
            sum.text = receipt.shop.sum
        }
    }

    inner class SumViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val sum = itemView.tv_item_sum
        fun bind(sum: String) {

        }
    }

    class SeparatorViewHolder(view: View): RecyclerView.ViewHolder(view)
}