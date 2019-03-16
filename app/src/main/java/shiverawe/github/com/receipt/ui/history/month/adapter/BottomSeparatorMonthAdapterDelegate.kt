package shiverawe.github.com.receipt.ui.history.month.adapter

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import shiverawe.github.com.receipt.R
import shiverawe.github.com.receipt.entity.receipt.month.ReceiptMonth
import shiverawe.github.com.receipt.ui.base.adapter.AdapterDelegate

class BottomSeparatorMonthAdapterDelegate(override var viewType: Int): AdapterDelegate<ReceiptMonth> {

    override fun isForViewType(items: ArrayList<ReceiptMonth>, position: Int): Boolean {
        return items[position].viewType == viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return BottomSeparatorMonthViewHolder(getLayout(parent, R.layout.item_month_bottom_separator))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, items: ArrayList<ReceiptMonth>, position: Int) {}

    class BottomSeparatorMonthViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

}