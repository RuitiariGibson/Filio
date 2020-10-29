package com.gibsoncodes.filio.binding

import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.graphics.drawable.toDrawable
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.gibsoncodes.filio.commons.CircleProgressBar
import com.gibsoncodes.filio.models.CategoriesModel
import com.gibsoncodes.filio.models.StorageStatisticsModel
import kotlin.math.round

@BindingAdapter("loadThumbnail","fileBitmap")
fun AppCompatImageView.loadFileThumbnail(uri:Uri, bitmap:Bitmap?){
    Glide.with(this.context)
        .load(uri) // try to see if the uri can point us to the file icon
        .error(bitmap?.toDrawable(this.resources)) // fall back to the default file icon
        .into(this)

}

@BindingAdapter("storageStatistics")
fun CircleProgressBar.setProgress( storageStatisticsModel: StorageStatisticsModel){

    val percentage = (storageStatisticsModel.usedUpStorage.toFloat()/storageStatisticsModel.totalStorage.toFloat())*100f
    this.setProgress(round(percentage.toDouble()).toFloat())
    this.setMax(100)
    this.setMin(1)

}
@BindingAdapter("storageStatus")
fun AppCompatTextView.setStorageStatus(storageStatisticsModel: StorageStatisticsModel){
    val status = "Total used ${storageStatisticsModel.usedUpStorage}GB/${storageStatisticsModel.totalStorage}GB"
    this.text=status
}
@BindingAdapter("itemsTotal")
fun AppCompatTextView.setNumberOfItems(total:List<CategoriesModel>){
    val status = " ${total.size} Items"
    this.text=status
}