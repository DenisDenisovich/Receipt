package shiverawe.github.com.receipt.ui.history.month.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_month_sum.view.*
import shiverawe.github.com.receipt.R
import shiverawe.github.com.receipt.entity.receipt.month.ReceiptMonth
import shiverawe.github.com.receipt.ui.base.adapter.AdapterDelegate

class SumMonthAdapterDelegate(override var viewType: Int) : AdapterDelegate<ReceiptMonth> {

    override fun isForViewType(items: ArrayList<ReceiptMonth>, position: Int): Boolean {
        return items[position].viewType == viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return SumMonthViewHolder(getLayout(parent, R.layout.item_month_sum))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, items: ArrayList<ReceiptMonth>, position: Int) {
        (holder as SumMonthViewHolder).bind(items[position])
    }

    class SumMonthViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val totalSum = itemView.tv_item_sum_total_sum
        fun bind(receipt: ReceiptMonth) {
            totalSum.text = receipt.shop.sum
        }
    }
}