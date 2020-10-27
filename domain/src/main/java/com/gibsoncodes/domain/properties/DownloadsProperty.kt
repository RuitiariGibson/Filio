package com.gibsoncodes.domain.properties

import com.gibsoncodes.domain.models.Downloads
import com.gibsoncodes.domain.repo.DocumentsRepo

class DownloadsProperty (private val repo:DocumentsRepo):ReadOnlyProperty<List<Downloads>>{
    override suspend fun invoke(): List<Downloads> {
        return repo.loadDownloadFiles()
    }
}