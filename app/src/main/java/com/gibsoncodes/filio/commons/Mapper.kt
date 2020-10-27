package com.gibsoncodes.filio.commons

import com.gibsoncodes.domain.models.*
import com.gibsoncodes.filio.models.*

fun Images.toImagesModel():ImagesModel= ImagesModel(this.imageName,
this.imageSize,this.dateAdded,this.contentUri)

fun Videos.toVideosModel():VideosModel = VideosModel(this.id,
this.name,this.size,this.uri,this.dateAdded,this.thumbnail)

fun Audios.toAudiosModel():AudiosModel = AudiosModel(this.id,
    this.name,this.size,this.uri,this.dateAdded,this.thumbnail)
fun StorageStatistics.toStorageStatisticsModel():StorageStatisticsModel =
    StorageStatisticsModel(this.usedUpStorage,this.totalStorage)
fun Downloads.toDownloadsModel(): DownloadsModel= DownloadsModel(this.fileName,
this.fileSize,this.fileExtension,this.bitmap,this.dateAdded, this.uri)