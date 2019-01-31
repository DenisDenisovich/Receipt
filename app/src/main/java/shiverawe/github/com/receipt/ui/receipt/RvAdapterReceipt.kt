package shiverawe.github.com.receipt.ui.receipt

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_product.view.*
import shiverawe.github.com.receipt.R
import shiverawe.github.com.receipt.entity.Product
import java.math.BigDecimal
import java.math.RoundingMode



class RvAdapterReceipt(val products: ArrayList<Product>): RecyclerView.Adapter<RvAdapterReceipt.ViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val holder = ViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.item_product, p0, false))
        holder.name.setOnClickListener { it.isSelected = !it.isSelected }
        return holder
    }

    override fun getItemCount() = products.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(products[position])
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val name = view.tv_item_product_name
        private val amount = view.tv_item_product_amount
        private val price = view.tv_item_product_price

        @SuppressLint("SetTextI18n")
        fun bind(product: Product) {
            name.isSelected = false
            name.text = product.text
            val amountNumber = BigDecimal(product.amount).setScale(3, RoundingMode.DOWN).toDouble()
            val amountString = if (amountNumber == Math.floor(amountNumber)) amountNumber.toInt().toString()
            else amountNumber.toString()
            amount.text = amountString
            price.text = BigDecimal(product.price).setScale(2, RoundingMode.DOWN).toString() + " p"
        }
    }
}