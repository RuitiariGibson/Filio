package com.gibsoncodes.filio.di

import com.gibsoncodes.filio.features.viewmodels.*
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val imagesViewModelModule = module {
    viewModel { ImagesViewModel(androidApplication(), get(named("images"))) }
}
val downloadViewModelModule = module {
    viewModel { DownloadsViewModel(androidApplication(), get(named("downloads"))) }

}
val audiosViewModelModule = module {
    viewModel { AudioViewModel(androidApplication(), get(named("audios"))) }
}
val videosViewModelModule = module{
    viewModel { VideosViewModel(androidApplication(), get(named("videos")))}
}
val storageStatisticsViewModelModule = module{
    viewModel { StorageStatisticsViewModel(get(named("stats"))) }
}