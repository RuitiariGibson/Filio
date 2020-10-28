package com.gibsoncodes.data.source.repo

import com.gibsoncodes.domain.models.StorageStatistics
import com.gibsoncodes.data.source.DataSource
import com.gibsoncodes.domain.repo.StatisticsRepo

class StatisticsRepoImpl(private val source:DataSource):
    StatisticsRepo {
    override suspend fun loadStorageStats(): StorageStatistics? {
        return source.loadStorageStats()
    }
}