package com.gibsoncodes.filio.models

import android.graphics.Bitmap
import android.net.Uri
import java.util.*

data class DownloadsModel(
    val fileName: String, val fileSize: Int, val fileExtension: String,
    var bitmap: Bitmap?,
    val dateAdded: Date, val uri: Uri
)