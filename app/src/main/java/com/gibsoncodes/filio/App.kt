package com.gibsoncodes.filio

import android.app.Application
import com.gibsoncodes.di.modules.recentFilesModule
import com.gibsoncodes.filio.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App:Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(listOf(
                com.gibsoncodes.di.modules.propertiesModule,
                com.gibsoncodes.di.modules.mediaFilesRepoModule,
                com.gibsoncodes.di.modules.documentFilesModule,
                com.gibsoncodes.di.modules.storageStatisticsModule,
                com.gibsoncodes.di.modules.sourceModule, imagesViewModelModule, videosViewModelModule,
           audiosViewModelModule,
                com.gibsoncodes.di.modules.storageStatisticsModule, downloadViewModelModule,
                storageStatisticsViewModelModule ,
                com.gibsoncodes.di.modules.totalFileSizesModule, fileSizeViewModelModule,
                recentFilesModule, recentFilesViewModel
            ))
        }
    }
}