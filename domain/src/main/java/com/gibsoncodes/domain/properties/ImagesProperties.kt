package com.gibsoncodes.domain.properties

import com.gibsoncodes.domain.models.Images
import com.gibsoncodes.domain.repo.MediaFilesRepo

class ImagesProperties(private val repo:MediaFilesRepo):ReadOnlyProperty<List<Images>> {
    override suspend fun invoke(): List<Images> {
        return repo.loadImages()
    }
}