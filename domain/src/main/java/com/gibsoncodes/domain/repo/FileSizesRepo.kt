package com.gibsoncodes.domain.repo

import com.gibsoncodes.domain.models.TotalFileSizes

interface FileSizesRepo {
    suspend fun loadFileSizes():TotalFileSizes
}