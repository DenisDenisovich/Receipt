package shiverawe.github.com.receipt.ui.receipt.info.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_product.view.*
import shiverawe.github.com.receipt.R
import shiverawe.github.com.receipt.domain.entity.base.Product
import shiverawe.github.com.receipt.ui.App
import shiverawe.github.com.receipt.ui.base.adapter.AdapterDelegate
import shiverawe.github.com.receipt.utils.floorThree
import shiverawe.github.com.receipt.utils.floorTwo
import java.util.regex.Pattern

class ItemProductDelegate(override var viewType: Int) : AdapterDelegate<Product> {
    override fun isForViewType(items: ArrayList<Product>, position: Int): Boolean {
        return true
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return ProductViewHolder(getLayout(parent, R.layout.item_product))
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        items: ArrayList<Product>,
        position: Int) {
        (holder as ProductViewHolder).bind(items[position])
    }

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val amountEmptyPatter = Pattern.compile("^[0-1](\\.0{0,3})?")
        private val title = itemView.tv_item_product_title
        private val sum = itemView.tv_item_product_sum
        private val sumDescription = itemView.tv_item_product_sum_description

        @SuppressLint("SetTextI18n")
        fun bind(product: Product) {
            title.text = product.text
            val price = product.price.floorTwo()
            val amount = product.amount.floorThree()
            if (!amountEmptyPatter.matcher(amount).matches()) {
                sumDescription.visibility = View.VISIBLE
                val amountStr: String = amount.replace("\\.0+".toRegex(), "")
                sumDescription.text = "$amountStr X $price ${getRuble()}"
                val totalSum = (amount.toDouble() * price.toDouble()).floorTwo()
                sum.text = "$totalSum ${getRuble()}"
            } else {
                sumDescription.visibility = View.GONE
                sum.text = "$price ${getRuble()}"
            }
        }

        private fun getRuble(): String = App.appContext.resources.getString(R.string.rubleSymbolJava)

    }


}