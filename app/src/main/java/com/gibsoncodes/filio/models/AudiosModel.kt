package com.gibsoncodes.filio.models

import android.graphics.Bitmap
import android.net.Uri
import java.util.*

data class AudiosModel (val id: Long, val name: String,
                        val size: Int?, val uri: Uri?, val dateAdded: Date?,
                        var thumbnail: Bitmap?
)