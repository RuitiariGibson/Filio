package com.gibsoncodes.filio.binding

import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.graphics.drawable.toDrawable
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("loadThumbnail","fileBitmap")
fun AppCompatImageView.loadFileThumbnail(uri:Uri, bitmap:Bitmap?){
    Glide.with(this.context)
        .load(uri) // try to see if the uri can point us to the file icon
        .error(bitmap?.toDrawable(this.resources)) // fall back to the default file icon
        .into(this)

}