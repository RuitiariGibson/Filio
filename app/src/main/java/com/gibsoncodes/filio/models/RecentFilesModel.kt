package com.gibsoncodes.filio.models

import android.media.browse.MediaBrowser
import android.net.Uri
import androidx.recyclerview.widget.DiffUtil
import java.util.*

data class RecentFilesModel(val fileId:Long, val fileName:String,
                       val fileSize:Int, val fileUri: Uri, val dateAdded: Date, val fileType:Int) {
    companion object{
        val diffUtil = object:DiffUtil.ItemCallback<RecentFilesModel> (){
            override fun areContentsTheSame(
                oldItem: RecentFilesModel,
                newItem: RecentFilesModel
            ): Boolean {
                return oldItem.fileId==newItem.fileId
            }

            override fun areItemsTheSame(
                oldItem: RecentFilesModel,
                newItem: RecentFilesModel
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}