package com.gibsoncodes.domain.repo

import com.gibsoncodes.domain.models.RecentFiles

interface RecentFilesRepo
{
    suspend fun loadRecentFiles():List<RecentFiles>
}