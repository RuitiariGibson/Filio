package com.gibsoncodes.domain.repo

import com.gibsoncodes.domain.models.Downloads
import com.gibsoncodes.data.source.DataSource

class DocumentFilesRepoImpl(private val source:DataSource):DocumentsRepo {
    override suspend fun loadDownloadFiles(): List<Downloads> {
        return source.loadDownloadFilesFromDisc()
    }
}