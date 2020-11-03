package com.gibsoncodes.filio.binding

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.text.format.Formatter
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.gibsoncodes.filio.R
import com.gibsoncodes.filio.commons.CircleProgressBar
import com.gibsoncodes.filio.commons.loadThumbnail
import com.gibsoncodes.filio.models.CategoriesModel
import com.gibsoncodes.filio.models.RecentFilesModel
import com.gibsoncodes.filio.models.StorageStatisticsModel
import com.google.android.material.drawable.DrawableUtils
import com.google.android.material.imageview.ShapeableImageView
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.round

@BindingAdapter("loadThumbnail","fileBitmap")
fun ShapeableImageView.loadFileThumbnail(uri:Uri, fileBitmap: Bitmap?){
        Glide.with(this.context)
            .load(uri)
            .centerCrop()
            .error(fileBitmap?.toDrawable(this.resources))
            .into(this)
}
@BindingAdapter("loadFile")
fun ShapeableImageView.loadFile(uri:Uri){
    Glide.with(this.context)
        .load(uri)
        .centerCrop()
        .override(200, 200)
        .into(this)
}
@BindingAdapter("formatSizeToString")
fun AppCompatTextView.formatSizeToString(size:Int?){
    size?.let{
       val formattedSize= Formatter.formatFileSize(this.context, size.toLong())
        this.text =formattedSize
    }
}
@BindingAdapter("dateModified")
fun AppCompatTextView.dateModified(date:Date?) {
    date?.let {
        val timeStamp = (date.time * 1000)
        // dd.MM.yyyy
        SimpleDateFormat("dd.MM.yyyy", Locale.US).format(timeStamp)
            .let { stringDate ->
                this.text = stringDate
            }

    }
}
@BindingAdapter("dateAdded")
fun AppCompatTextView.dateAddedFormatter(date:Date?){
    date?.let{
        val pattern = "dd.MM.yyyy"
        val timeStamp =(date.time * 1000)
        SimpleDateFormat(pattern, Locale.US).format(timeStamp)
            .let{
                stringDate->
                this.text = stringDate
            }
    }
}
@BindingAdapter("loadCategoryItems")
fun AppCompatTextView.loadCategoryItems(fileSize:Int?){
    fileSize?.let{
        val finalFileSize= Formatter.formatFileSize(this.context,fileSize.toLong())
        this.text = finalFileSize
    }
}
@BindingAdapter("showRecentFilesLabel")
fun AppCompatTextView.showRecentFilesLabel(list:List<RecentFilesModel>?){
    val show = list!=null && list.isNotEmpty()
    if (show){
        this.text= this.resources.getString(R.string.recent_files)
    }else this.visibility= View.GONE

}
@BindingAdapter("loadRecentFilesCategoryThumbnail")
fun ShapeableImageView.loadThumbnail(mediaType:Int){
    var thumbnail:Drawable? = null
    when(mediaType){
        2->{
            thumbnail = ContextCompat.getDrawable(this.context, R.drawable.audio_type)
        }
        3->{
            thumbnail = ContextCompat.getDrawable(this.context, R.drawable.video_file_type)
        }
        1->{
            thumbnail = ContextCompat.getDrawable(this.context, R.drawable.imagetype)
        }

    }
    Glide.with(this.context)
        .load(thumbnail)
        .override(Target.SIZE_ORIGINAL)
        .centerInside()
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(this)

}

/**
 * Media types as per the documentation
 * 2 == audio
 * 1 == image
 * 3 == video
 */
@BindingAdapter("loadRecentFilesThumbnails", "recentFilesUri")
fun ShapeableImageView.loadRecentFilesThumbnail(mediaType:Int, uri:Uri){
    var thumbnail:Drawable? = null
    when(mediaType){
        2->{
           thumbnail = ContextCompat.getDrawable(this.context, R.drawable.audio_folder_icon)
        }
        1->{
            thumbnail=ContextCompat.getDrawable(this.context, R.drawable.picture_folder_icon)
        }
        3->{
            thumbnail=ContextCompat.getDrawable(this.context, R.drawable.videos_folder_icon)
        }
    }
    Glide.with(this.context)
        .load(uri)
        .centerCrop()
        .override(Target.SIZE_ORIGINAL)
        .error(thumbnail)
        .into(this)
}

@BindingAdapter("totalStorageStatisticsStatus")
fun AppCompatTextView.loadStorageStatusPerStatistics(storageStatisticsModel:StorageStatisticsModel?){
    storageStatisticsModel?.let {

        val usedUp = storageStatisticsModel.usedUpStorage.removeRange(
            (storageStatisticsModel.usedUpStorage.length - 3),
            storageStatisticsModel.usedUpStorage.length
        )
        val total = storageStatisticsModel.totalStorage.removeRange(
            storageStatisticsModel.totalStorage.length - 3,
            storageStatisticsModel.totalStorage.length
        )
        val availableMemory = round(total.toFloat() - usedUp.toFloat())

        val status = if (availableMemory >= 5){
            "storage status : good"
        }else{
            "storage status : running low on storage"
        }

        this.text = status
    }
}
@BindingAdapter("loadCategoryThumbnail")
fun AppCompatImageView.loadCategoryThumbnail(categoryName:String){
    var thumbnail:Drawable? = null
    when(categoryName){
        "Images"->{
            thumbnail = ResourcesCompat.getDrawable(resources,
            R.drawable.picture_folder_icon,null)
        }
        "Audio"->{
            thumbnail = ResourcesCompat.getDrawable(resources, R.drawable.audio_folder_icon,
            null)
        }
        "Videos"->{
            thumbnail = ResourcesCompat.getDrawable(resources,
            R.drawable.videos_folder_icon,
            null)
        }
        "Downloads"->{
            thumbnail=ResourcesCompat.getDrawable(resources, R.drawable.download_folder_icon,
            null)
        }
    }
    Glide.with(this.context)
        .load(thumbnail)
        .into(this)
}
@BindingAdapter("storageStatistics")
fun CircleProgressBar.setProgress( storageStatisticsModel: StorageStatisticsModel?){
    storageStatisticsModel?.let{
        val usedUp=storageStatisticsModel.usedUpStorage.removeRange((storageStatisticsModel.usedUpStorage.length-3),storageStatisticsModel.usedUpStorage.length)
        val total = storageStatisticsModel.totalStorage.removeRange(storageStatisticsModel.totalStorage.length-3, storageStatisticsModel.totalStorage.length)
       // storageStatisticsModel.usedUpStorage.last()
        val percentage = (usedUp.toFloat()*100f)/total.toFloat()
        this.setProgress(percentage.toDouble().toFloat())
        this.setMax(100)

    }


}
@BindingAdapter("storageStatus")
fun AppCompatTextView.setStorageStatus(storageStatisticsModel: StorageStatisticsModel?){
    storageStatisticsModel?.let{
        val status = "Total used ${storageStatisticsModel.usedUpStorage}/ ${storageStatisticsModel.totalStorage}"
        this.text=status
    }

}
@BindingAdapter("itemsTotal")
fun AppCompatTextView.setNumberOfItems(total:List<CategoriesModel>?){
    total?.let{
        val status = " ${total.size} Items"
        this.text=status
    }

}