package com.gibsoncodes.filio

import android.app.Application
import com.gibsoncodes.domain.di.*
import com.gibsoncodes.filio.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App:Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(listOf(propertiesModule,
            mediaFilesRepoModule, documentFilesModule, storageStatisticsModule,
            sourceModule, imagesViewModelModule, videosViewModelModule,
           audiosViewModelModule, storageStatisticsModule, downloadViewModelModule, storageStatisticsViewModelModule ))
        }
    }
}