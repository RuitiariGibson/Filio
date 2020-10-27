package com.gibsoncodes.domain.properties

import com.gibsoncodes.domain.models.StorageStatistics
import com.gibsoncodes.domain.repo.StatisticsRepo

class StatisticsProperty (private val statsRepo:StatisticsRepo):ReadOnlyProperty<StorageStatistics?>{
    override suspend fun invoke(): StorageStatistics? {
        return statsRepo.loadStorageStats()
    }
}