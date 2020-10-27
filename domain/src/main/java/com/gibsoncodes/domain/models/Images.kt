package com.gibsoncodes.domain.models

import android.net.Uri
import java.util.*

data class Images (val imageId:Long,
                   val imageName:String, val imageSize:Int, val contentUri:Uri, val dateAdded:Date)