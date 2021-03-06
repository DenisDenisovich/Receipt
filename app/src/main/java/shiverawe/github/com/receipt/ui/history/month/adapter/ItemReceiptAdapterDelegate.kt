package shiverawe.github.com.receipt.ui.history.month.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_receipt.view.*
import shiverawe.github.com.receipt.R
import shiverawe.github.com.receipt.domain.entity.base.ReceiptHeader
import shiverawe.github.com.receipt.ui.App
import shiverawe.github.com.receipt.ui.base.adapter.AdapterDelegate
import shiverawe.github.com.receipt.utils.floorTwo
import java.text.SimpleDateFormat
import java.util.*

class ItemReceiptAdapterDelegate(override var viewType: Int) : AdapterDelegate<ReceiptHeader> {
    private val timeFormatter = SimpleDateFormat("dd.MM_HH:mm", Locale("ru"))

    override fun isForViewType(items: ArrayList<ReceiptHeader>, position: Int): Boolean {
        return items[position].receiptId != -1L
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return ItemReceiptViewHolder(getLayout(parent, R.layout.item_receipt))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, items: ArrayList<ReceiptHeader>, position: Int) {
        (holder as ItemReceiptViewHolder).bind(items[position])
    }

    inner class ItemReceiptViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val shopLetter = itemView.tv_item_month_image
        private val title = itemView.tv_item_month_title
        private val sum = itemView.tv_item_month_sum
        private val time = itemView.tv_item_month_time

        private val titlePlaceholder = itemView.context.getString(R.string.shop_placeholder)

        @SuppressLint("SetTextI18n")
        fun bind(receipt: ReceiptHeader) {
            time.text = timeFormatter.format(Date(receipt.shop.date)).split("_")[1]
            val titleText = receipt.shop.title.ifEmpty { titlePlaceholder }
            title.text = titleText

            sum.text = receipt.shop.sum.floorTwo() + " " + itemView.context.getString(R.string.rubleSymbolJava)
            shopLetter.text = titleText.first().toString()
        }
    }
}