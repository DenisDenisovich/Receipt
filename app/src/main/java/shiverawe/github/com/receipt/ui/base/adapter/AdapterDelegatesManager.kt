package shiverawe.github.com.receipt.ui.base.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup

class AdapterDelegatesManager<T> {

    private var delegates: HashMap<Int, AdapterDelegate<T>> = HashMap()

    fun addDelegate(delegate: AdapterDelegate<T>) {
        delegates[delegate.viewType] = delegate
    }

    fun getViewType(items: ArrayList<T>, position: Int): Int? {
        for (index in delegates.keys) {
            if (delegates[index]!!.isForViewType(items, position))
                return index
        }
        return null
    }

    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return delegates[viewType]!!.onCreateViewHolder(parent)
    }

    fun onBindViewHolder(holder: RecyclerView.ViewHolder, items: ArrayList<T>, position: Int) {
        delegates[holder.itemViewType]!!.onBindViewHolder(holder, items, position)
    }
}