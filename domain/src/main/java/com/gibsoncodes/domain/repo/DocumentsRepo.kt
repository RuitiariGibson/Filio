package com.gibsoncodes.domain.repo

import com.gibsoncodes.domain.models.Downloads

interface DocumentsRepo {
    suspend fun loadDownloadFiles():List<Downloads>
}