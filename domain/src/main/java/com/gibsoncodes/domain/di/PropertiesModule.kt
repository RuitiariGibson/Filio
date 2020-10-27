package com.gibsoncodes.domain.di

import com.gibsoncodes.domain.properties.*
import com.gibsoncodes.domain.repo.DocumentsRepo
import com.gibsoncodes.domain.repo.MediaFilesRepo
import com.gibsoncodes.domain.repo.StatisticsRepo
import org.koin.core.qualifier.named
import org.koin.dsl.module

val propertiesModule = module {
    single (named("audios")){ provideAudioProperty(get()) }
    single (named("downloads")){ provideDownloadsProperty(get()) }
    single (named("videos")){ provideVideosProperty(get()) }
    single (named("images")){ provideImageProperty(get()) }
    single (named("stats")){ provideStorageStatsProperty(get()) }
}
private fun provideImageProperty(repo:MediaFilesRepo):ImagesProperties{
    return ImagesProperties(repo)
}
private fun provideVideosProperty(repo:MediaFilesRepo):VideosProperties{
    return VideosProperties(repo)
}
private fun provideAudioProperty(repo:MediaFilesRepo):AudiosProperties{
    return AudiosProperties(repo)
}
private fun provideDownloadsProperty(repo:DocumentsRepo):DownloadsProperty{
    return DownloadsProperty(repo)
}
private fun provideStorageStatsProperty(repo:StatisticsRepo):StatisticsProperty{
    return StatisticsProperty(repo)
}