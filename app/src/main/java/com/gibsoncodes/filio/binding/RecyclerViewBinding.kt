package com.gibsoncodes.filio.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gibsoncodes.filio.adapters.CategoriesAdapter
import com.gibsoncodes.filio.adapters.RecentFilesAdapter
import com.gibsoncodes.filio.models.CategoriesModel
import com.gibsoncodes.filio.models.RecentFilesModel


@BindingAdapter("storageCategoryAdapter")
fun RecyclerView.bindStorageCategoryRvAdapter(adapter:RecyclerView.Adapter<*>){
    this.adapter = adapter
}

@BindingAdapter("storageCategoryList")
fun RecyclerView.bindStorageCategoryRvToList(list:List<CategoriesModel>?){
    list?.let{
        (this.adapter as CategoriesAdapter).submitList(it)

    }
}
@BindingAdapter("recentFilesList")
fun RecyclerView.bindRecentFilesRvToList(list:List<RecentFilesModel>?){
    list?.let{
        (this.adapter as RecentFilesAdapter).submitList(it)
    }
}
@BindingAdapter("recentFilesAdapter")
fun RecyclerView.bindRecentFileAdapter(adapter:RecyclerView.Adapter<*>){
    this.adapter= adapter
}