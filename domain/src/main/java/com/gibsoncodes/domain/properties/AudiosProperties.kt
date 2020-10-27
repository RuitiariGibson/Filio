package com.gibsoncodes.domain.properties

import com.gibsoncodes.domain.models.Audios
import com.gibsoncodes.domain.repo.MediaFilesRepo

class AudiosProperties (private val repo:MediaFilesRepo):ReadOnlyProperty<List<Audios>>{
    override suspend fun invoke(): List<Audios> {
        return repo.loadAudioFiles()
    }
}