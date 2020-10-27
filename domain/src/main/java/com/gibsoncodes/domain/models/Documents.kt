package com.gibsoncodes.domain.models

import android.graphics.Bitmap
import android.net.Uri
import java.util.*

data class Documents(
    val fileId: Int,
    val fileName: String,
    val fileSize: Int,
    val fileType: String,
    val fileThumbnail: Bitmap?,
    val dateAdded: Date,
    val contentUri: Uri
)