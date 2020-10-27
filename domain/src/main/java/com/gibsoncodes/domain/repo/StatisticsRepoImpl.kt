package com.gibsoncodes.domain.repo

import com.gibsoncodes.domain.models.StorageStatistics
import com.gibsoncodes.data.source.DataSource

class StatisticsRepoImpl(private val source:DataSource):StatisticsRepo {
    override suspend fun loadStorageStats(): StorageStatistics? {
        return source.loadStorageStats()
    }
}