package com.gibsoncodes.domain.properties

import com.gibsoncodes.domain.models.RecentFiles
import com.gibsoncodes.domain.repo.RecentFilesRepo

class RecentFilesProperties(private val recentFiles:RecentFilesRepo):ReadOnlyProperty<List<RecentFiles>> {
    override suspend fun invoke(): List<RecentFiles> {
        return recentFiles.loadRecentFiles()
    }
}