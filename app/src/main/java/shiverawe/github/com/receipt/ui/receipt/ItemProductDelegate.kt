package shiverawe.github.com.receipt.ui.receipt

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_product.view.*
import shiverawe.github.com.receipt.R
import shiverawe.github.com.receipt.entity.receipt.base.Product
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

        @SuppressLint("SetTextI18n")
        fun bind(product: Product) {
            title.text = product.text
            sum.text = BigDecimal(product.price).setScale(2, RoundingMode.DOWN).toString() + " " + App.appContext.resources.getString(R.string.rubleSymbolJava)
        }
    }
}