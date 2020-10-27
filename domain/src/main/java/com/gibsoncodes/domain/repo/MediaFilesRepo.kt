package com.gibsoncodes.domain.repo

import com.gibsoncodes.domain.models.Audios
import com.gibsoncodes.domain.models.Images
import com.gibsoncodes.domain.models.Videos

interface MediaFilesRepo {
    suspend fun loadImages():List<Images>
    suspend fun loadAudioFiles():List<Audios>
    suspend fun loadVideoFiles():List<Videos>

}