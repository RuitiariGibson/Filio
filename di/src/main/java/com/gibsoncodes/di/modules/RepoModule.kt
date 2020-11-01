package com.gibsoncodes.di.modules

import com.gibsoncodes.data.source.DataSource
import com.gibsoncodes.data.source.repo.*
import com.gibsoncodes.domain.repo.*
import org.koin.dsl.module

val mediaFilesRepoModule = module {
    single<MediaFilesRepo> { provideMediaFilesRepo(get()) }
}
val storageStatisticsModule = module{
    single<StatisticsRepo> {
        provideStorageStatisticsRepo(
            get()
        )
    }
}
val documentFilesModule = module{
    single<DocumentsRepo> { provideDocumentsRepo(get()) }
}
val totalFileSizesModule = module{
    single<FileSizesRepo> { FileSizeRepoImpl(get()) }
}
private fun provideMediaFilesRepo(source:DataSource): MediaFilesRepoImpl {
    return MediaFilesRepoImpl(source)
}
private fun provideStorageStatisticsRepo(source:DataSource): StatisticsRepoImpl {
    return StatisticsRepoImpl(source)
}
private fun provideDocumentsRepo(source:DataSource): DocumentFilesRepoImpl {
    return DocumentFilesRepoImpl(source)
}
val recentFilesModule = module{
    single<RecentFilesRepo> {
        RecentFilesRepoImpl(get())
    }
}
