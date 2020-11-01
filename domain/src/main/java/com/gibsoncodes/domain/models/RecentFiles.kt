package com.gibsoncodes.domain.models

import android.net.Uri
import java.util.*

data class RecentFiles(val fileId:Long, val fileName:String,
                       val fileSize:Int, val fileUri: Uri, val dateAdded: Date, val fileType:Int)