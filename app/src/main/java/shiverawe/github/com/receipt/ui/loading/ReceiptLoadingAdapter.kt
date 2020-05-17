package shiverawe.github.com.receipt.ui.loading

import android.annotation.SuppressLint
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_receipt_loading.view.btn_delete
import kotlinx.android.synthetic.main.item_receipt_loading.view.tv_date
import kotlinx.android.synthetic.main.item_receipt_loading.view.tv_sum
import shiverawe.github.com.receipt.R
import shiverawe.github.com.receipt.domain.entity.base.ReceiptHeader
import shiverawe.github.com.receipt.utils.color
import shiverawe.github.com.receipt.utils.toPixels
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ReceiptLoadingAdapter(
    private val onDelete: (receiptHeader: ReceiptHeader) -> Unit
) : RecyclerView.Adapter<ReceiptLoadingAdapter.ReceiptLoadingViewHolder>() {

    private val items: ArrayList<ReceiptHeader> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceiptLoadingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_receipt_loading, parent, false)

        return ReceiptLoadingViewHolder(view).apply {
            btnDelete.setOnClickListener {
                onDelete(items[adapterPosition])
            }
        }
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ReceiptLoadingViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun setReceipts(receipts: List<ReceiptHeader>) {
        items.clear()
        items.addAll(receipts)
        notifyDataSetChanged()
    }

    class ReceiptLoadingViewHolder(
        view: View,
        val btnDelete: ImageView = view.btn_delete,
        private val date: TextView = view.tv_date,
        private val sum: TextView = view.tv_sum
    ) : RecyclerView.ViewHolder(view) {

        private val timeFormatter = SimpleDateFormat("EEEE, dd.MM HH:mm", Locale("ru"))

        @SuppressLint("DefaultLocale")
        fun bind(receipt: ReceiptHeader) {
            date.text = getDateSpannable(receipt.shop.date)
            sum.text = itemView.context.getString(R.string.sum_placeholder, receipt.shop.sum)
        }

        @SuppressLint("DefaultLocale")
        private fun getDateSpannable(date: Long): SpannableString {
            val dateText = timeFormatter.format(date).capitalize()
            val timeStart = dateText.lastIndexOf(' ')

            return SpannableString(dateText).apply {
                setSpan(
                    ForegroundColorSpan(itemView.context.color(R.color.textGray)),
                    timeStart,
                    dateText.length,
                    Spannable.SPAN_EXCLUSIVE_INCLUSIVE
                )
                setSpan(AbsoluteSizeSpan(14.toPixels()),
                    timeStart,
                    dateText.length,
                    Spannable.SPAN_EXCLUSIVE_INCLUSIVE
                )
            }
        }
    }
}