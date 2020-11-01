package com.gibsoncodes.filio.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gibsoncodes.filio.adapters.*
import com.gibsoncodes.filio.models.*


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
@BindingAdapter("downloadsAdapter")
fun RecyclerView.bindDownloadsAdapterToRv(adapter:RecyclerView.Adapter<*>){
    this.adapter = adapter
}

@BindingAdapter("downloadsList")
fun RecyclerView.bindDownloadsListToAdapter(list:List<DownloadsModel>?){
    list?.let{
        (this.adapter as DownloadsAdapter).submitList(it)

    }
}
@BindingAdapter("imagesAdapter")
fun RecyclerView.bindImagesRvToAdapter(adapter:RecyclerView.Adapter<*>){
    this.adapter = adapter
}

@BindingAdapter("imagesList")
fun RecyclerView.bindImagesRvToList(list:List<ImagesModel>?){
    list?.let{
        (this.adapter as ImagesAdapter).submitList(it)

    }
}
@BindingAdapter("audioAdapter")
fun RecyclerView.bindAudiosRvToAdapter(adapter:RecyclerView.Adapter<*>){
    this.adapter = adapter
}

@BindingAdapter("audioList")
fun RecyclerView.bindAudiosRvToList(list:List<AudiosModel>?){
    list?.let{
        (this.adapter as AudiosAdapter).submitList(it)

    }
}
@BindingAdapter("videoAdapter")
fun RecyclerView.bindVideoAdapterToRv(adapter:RecyclerView.Adapter<*>){
    this.adapter = adapter
}

@BindingAdapter("videosList")
fun RecyclerView.bindVideosRvToList(list:List<VideosModel>?){
    list?.let{
        (this.adapter as VideosAdapter).submitList(it)

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