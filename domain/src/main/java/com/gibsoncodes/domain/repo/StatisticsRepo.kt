package com.gibsoncodes.domain.repo

import com.gibsoncodes.domain.models.StorageStatistics

interface StatisticsRepo {
    suspend fun loadStorageStats(): StorageStatistics?

}