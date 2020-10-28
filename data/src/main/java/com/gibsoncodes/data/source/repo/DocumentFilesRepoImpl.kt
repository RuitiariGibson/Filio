package com.gibsoncodes.data.source.repo

import com.gibsoncodes.domain.models.Downloads
import com.gibsoncodes.data.source.DataSource
import com.gibsoncodes.domain.repo.DocumentsRepo

class DocumentFilesRepoImpl(private val source:DataSource):
    DocumentsRepo {
    override suspend fun loadDownloadFiles(): List<Downloads> {
        return source.loadDownloadFilesFromDisc()
    }
}