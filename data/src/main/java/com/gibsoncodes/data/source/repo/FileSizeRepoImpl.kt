package com.gibsoncodes.data.source.repo

import com.gibsoncodes.data.source.DataSource
import com.gibsoncodes.domain.models.TotalFileSizes
import com.gibsoncodes.domain.repo.FileSizesRepo

class FileSizeRepoImpl(val source:DataSource):
    FileSizesRepo {
    override suspend fun loadFileSizes(): TotalFileSizes {
        return source.loadFileSizes()
    }
}