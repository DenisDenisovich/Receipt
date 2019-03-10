package shiverawe.github.com.receipt.ui.base.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import shiverawe.github.com.receipt.entity.receipt.month.ReceiptMonth

interface AdapterDelegate<T> {
    var viewType: Int
    fun isForViewType(items: ArrayList<T>, position: Int): Boolean
    fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder
    fun onBindViewHolder(holder: RecyclerView.ViewHolder, items: ArrayList<T>, position: Int)

    fun getLayout(parent: ViewGroup, resourceId: Int): View {
        return LayoutInflater.from(parent.context).inflate(resourceId, parent, false)
    }
}