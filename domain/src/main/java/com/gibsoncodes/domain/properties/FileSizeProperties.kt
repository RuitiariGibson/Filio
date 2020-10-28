package com.gibsoncodes.domain.properties

import com.gibsoncodes.domain.models.TotalFileSizes
import com.gibsoncodes.domain.repo.FileSizesRepo

class FileSizeProperties (private val repo:FileSizesRepo):ReadOnlyProperty<TotalFileSizes> {
    override suspend fun invoke(): TotalFileSizes {
        return repo.loadFileSizes()
    }
}