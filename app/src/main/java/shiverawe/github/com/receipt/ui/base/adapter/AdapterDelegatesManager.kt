package shiverawe.github.com.receipt.ui.base.adapter

import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.ViewGroup

class AdapterDelegatesManager<T> {

    private var delegates: SparseArray<AdapterDelegate<T>> = SparseArray()
    private var viewType = 0

    fun addDelegate(delegate: AdapterDelegate<T>) {
        delegates.put(viewType, delegate)
        viewType++
    }

    fun getViewType(items: ArrayList<T>, position: Int): Int? {
        for (index in 0 until viewType) {
            if (delegates[index].isForViewType(items, position))
                return index
        }
        return null
    }

    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return delegates[viewType].onCreateViewHolder(parent)
    }

    fun onBindViewHolder(holder: RecyclerView.ViewHolder, items: ArrayList<T>, position: Int) {
        delegates[holder.itemViewType].onBindViewHolder(holder, items, position)
    }
}