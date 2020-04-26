package shiverawe.github.com.receipt.ui.receipt.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import shiverawe.github.com.receipt.domain.entity.dto.base.Product
import shiverawe.github.com.receipt.ui.base.adapter.AdapterDelegatesManager

class ProductAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val delegateManager = AdapterDelegatesManager<Product>()
    private val items: ArrayList<Product> = ArrayList()

    init {
        delegateManager.addDelegate(ItemProductDelegate(0))
    }

    override fun onCreateViewHolder(parent: ViewGroup, vieType: Int): RecyclerView.ViewHolder {
        return delegateManager.onCreateViewHolder(parent, vieType)
    }

    override fun getItemViewType(position: Int) = 0

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        delegateManager.onBindViewHolder(holder, items, position)
    }

    fun setProducts(products: ArrayList<Product>) {
        items.addAll(products)
        notifyDataSetChanged()
    }
}