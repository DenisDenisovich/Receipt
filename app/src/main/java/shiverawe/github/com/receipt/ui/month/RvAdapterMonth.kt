package shiverawe.github.com.receipt.ui.month

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_month.view.*
import shiverawe.github.com.receipt.R
import shiverawe.github.com.receipt.entity.Shop

class RvAdapterMonth(val shops: ArrayList<Shop>, val shopIsClicked: (id: Long) -> Unit): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val SHOP_VIEW_TYPE = 0
    private val SEPARATOR_VIEW_TYPE = 1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val holder: RecyclerView.ViewHolder
        if (viewType == SHOP_VIEW_TYPE) {
            holder = ShopViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_month, parent))
            holder.itemView.setOnClickListener { shopIsClicked(holder.adapterPosition.toLong()) }
        } else
            holder = SeparatorViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.separator_month, parent))
        return holder
    }

    override fun getItemCount() = shops.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ShopViewHolder) {

/*
            holder.day.text = shops[position].date.toString()
            holder.dayDigit.text = shops[position].date.toString()
*/


            holder.name.text = shops[position].place
            holder.sum.text = shops[position].sum + " p"
        }
    }

    override fun getItemViewType(position: Int): Int {

        /*
        if (shops[position].date){} else {}
        */

        return SHOP_VIEW_TYPE
    }

    class ShopViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val dayDigit = view.tv_item_month_day_digit
        val day = view.tv_item_month_day
        val name = view.tv_item_month_name
        val sum = view.tv_item_month_sum
    }

    class SeparatorViewHolder(view: View): RecyclerView.ViewHolder(view)

}