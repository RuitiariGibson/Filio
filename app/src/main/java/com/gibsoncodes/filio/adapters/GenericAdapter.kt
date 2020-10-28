package com.gibsoncodes.filio.adapters

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
/**
A generic recycler view adapter
We need to do this since we cannot pre-determine the no of files a user has hence
to prevent calling [notifyDataSetChanged] every time a deletion/update occurs
we use the ListAdapter + AsyncDifferConfig
[DiffUtil.calculateDiff] runs on the main thread and if a large list is given then the difference may be potentially huge thus
slowing down the main thread. Hence the option is either to use [AsyncListDiffer] or use an [AsyncDifferConfig] to
ensure the whole process is done on a background thread.
@param <V> type of items to be submitted to the adapter
@param <T> type of the view data binding
 **/
abstract class GenericAdapter<V, T:ViewDataBinding>(callback:DiffUtil.ItemCallback<V>):ListAdapter<V, GenericViewHolder<T>>
    (AsyncDifferConfig.Builder<V>(callback).build()){
    override fun onBindViewHolder(holder: GenericViewHolder<T>, position: Int) {
        bind(holder.binding, getItem(position
        ))
        holder.binding.executePendingBindings()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenericViewHolder<T> {
        val binding =createBinding(parent)
        return GenericViewHolder(binding)
    }
    protected abstract fun createBinding(parent:ViewGroup):T
    protected abstract fun bind(binding:T, item:V)


}