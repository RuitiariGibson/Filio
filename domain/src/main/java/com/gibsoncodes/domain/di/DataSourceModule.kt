package com.gibsoncodes.domain.di

import com.gibsoncodes.data.source.DataSource
import org.koin.dsl.module

val sourceModule = module {
    single { DataSource(get()) }
}