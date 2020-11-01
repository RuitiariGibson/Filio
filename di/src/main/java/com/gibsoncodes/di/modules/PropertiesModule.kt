package com.gibsoncodes.di.modules

import com.gibsoncodes.domain.properties.*
import com.gibsoncodes.domain.repo.*
import org.koin.core.qualifier.named
import org.koin.dsl.module

val propertiesModule = module {
    single (named("audios")){ provideAudioProperty(get()) }
    single (named("downloads")){
        provideDownloadsProperty(
            get()
        )
    }
    single (named("videos")){
        provideVideosProperty(
            get()
        )
    }
    single (named("images")){ provideImageProperty(get()) }
    single (named("stats")){
        provideStorageStatsProperty(
            get()
        )
    }
    single (named("fileSize")){
        provideTotalFileSize(
            get()
        )
    }
    single(named("recentFiles")){
        provideRecentFilesProperties(get())
    }
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
private fun provideTotalFileSize(repo:FileSizesRepo):FileSizeProperties{
    return FileSizeProperties(repo)
}
private fun provideRecentFilesProperties(repo:RecentFilesRepo):RecentFilesProperties{
    return RecentFilesProperties(repo)
}