package shiverawe.github.com.receipt.ui.receipt

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_product.view.*
import shiverawe.github.com.receipt.R
import shiverawe.github.com.receipt.domain.entity.dto.base.Product
import shiverawe.github.com.receipt.ui.App
import shiverawe.github.com.receipt.ui.base.adapter.AdapterDelegate
import java.math.BigDecimal
import java.math.RoundingMode

class ItemProductDelegate(override var viewType: Int) : AdapterDelegate<Product> {
    override fun isForViewType(items: ArrayList<Product>, position: Int): Boolean {
        return true
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return ProductViewHolder(getLayout(parent, R.layout.item_product))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, items: ArrayList<Product>, position: Int) {
        (holder as ProductViewHolder).bind(items[position])
    }

    class ProductViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val title = itemView.tv_item_product_title
        private val sum = itemView.tv_item_product_sum
        private val sumDescription = itemView.tv_item_product_sum_description

        @SuppressLint("SetTextI18n")
        fun bind(product: Product) {
            title.text = product.text
            val price = BigDecimal(product.price).setScale(2, RoundingMode.DOWN).toDouble()
            val amount = BigDecimal(product.amount).setScale(2, RoundingMode.DOWN)
            if (amount.toDouble().compareTo(1.0) != 0 && amount.toDouble().compareTo(0.0) != 0) {
                sumDescription.visibility = View.VISIBLE
                val amountStr: String = if (amount.stripTrailingZeros().scale() <= 0) {
                    amount.toInt().toString()
                } else {
                    amount.toString()
                }
                sumDescription.text = "$amountStr X $price ${App.appContext.resources.getString(R.string.rubleSymbolJava)}"
                val totalSum = BigDecimal(amount.toDouble() * price).setScale(2, RoundingMode.HALF_UP)
                sum.text = "$totalSum ${App.appContext.resources.getString(R.string.rubleSymbolJava)}"
            } else {
                sumDescription.visibility = View.GONE
                sum.text = price.toString() + " " + App.appContext.resources.getString(R.string.rubleSymbolJava)
            }
        }
    }
}