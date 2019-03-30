package shiverawe.github.com.receipt.ui.history_v2.month_v2.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_receipt_v2.view.*
import shiverawe.github.com.receipt.R
import shiverawe.github.com.receipt.entity.receipt.month.ReceiptMonth_v2
import shiverawe.github.com.receipt.ui.base.adapter.AdapterDelegate
import java.text.SimpleDateFormat
import java.util.*

class ItemReceiptAdapterDelegate(override var viewType: Int) : AdapterDelegate<ReceiptMonth_v2> {
    private val timeFormatter = SimpleDateFormat("dd.MM_HH:mm", Locale("ru"))

    init {
        timeFormatter.timeZone = TimeZone.getTimeZone("UTC")
    }

    override fun isForViewType(items: ArrayList<ReceiptMonth_v2>, position: Int): Boolean {
        return items[position].receiptId != -1L
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return ItemReceiptViewHolder(getLayout(parent, R.layout.item_receipt_v2))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, items: ArrayList<ReceiptMonth_v2>, position: Int) {
        (holder as ItemReceiptViewHolder).bind(items[position])
    }

    inner class ItemReceiptViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val shopLetter = itemView.tv_item_month_image
        private val name = itemView.tv_item_month_name
        private val sum = itemView.tv_item_month_sum
        private val time = itemView.tv_item_month_time

        fun bind(receipt: ReceiptMonth_v2) {
            time.text = timeFormatter.format(Date(receipt.shop.date)).split("_")[1]
            name.text = receipt.shop.place
            sum.text = receipt.shop.sum
            if (receipt.shop.place.isNotEmpty()) {
                shopLetter.text = receipt.shop.place.first().toString()
            } else {
                shopLetter.text = "S"
            }
        }
    }

}