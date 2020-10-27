package com.gibsoncodes.domain.models

import android.graphics.Bitmap
import android.net.Uri
import java.util.*

data class Audios(
    val id: Long, val name: String,
    val size: Int?, val uri: Uri?, val dateAdded: Date?,
    val thumbnail: Bitmap?
)