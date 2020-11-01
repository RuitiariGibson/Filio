package com.gibsoncodes.data.source.repo

import com.gibsoncodes.data.source.DataSource
import com.gibsoncodes.domain.models.RecentFiles
import com.gibsoncodes.domain.repo.RecentFilesRepo

class RecentFilesRepoImpl(private val source:DataSource):RecentFilesRepo
{
    override suspend fun loadRecentFiles(): List<RecentFiles> {
        return source.loadRecentFiles()
    }
}