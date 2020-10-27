package com.gibsoncodes.domain.repo

import com.gibsoncodes.domain.models.Audios
import com.gibsoncodes.domain.models.Images
import com.gibsoncodes.domain.models.Videos
import com.gibsoncodes.data.source.DataSource

class MediaFilesRepoImpl(private val dataSource:DataSource):MediaFilesRepo {
    override suspend fun loadImages(): List<Images> {
        return dataSource.loadImagesFromDisc()
    }

    override suspend fun loadAudioFiles(): List<Audios> {
        return  dataSource.loadAudioFilesFromDisc()
    }

    override suspend fun loadVideoFiles(): List<Videos> {
        return dataSource.loadVideosMedia()
    }

}