package com.gibsoncodes.domain.properties

import com.gibsoncodes.domain.models.Videos
import com.gibsoncodes.domain.repo.MediaFilesRepo

class VideosProperties(private val repo:MediaFilesRepo):ReadOnlyProperty<List<Videos>> {
    override suspend fun invoke(): List<Videos> {
        return repo.loadVideoFiles()
    }
}