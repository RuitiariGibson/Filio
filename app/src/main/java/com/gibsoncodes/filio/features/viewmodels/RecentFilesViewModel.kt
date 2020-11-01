package com.gibsoncodes.filio.features.viewmodels

import android.app.Application
import android.provider.MediaStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gibsoncodes.domain.models.RecentFiles
import com.gibsoncodes.domain.properties.RecentFilesProperties
import com.gibsoncodes.filio.commons.toRecentFilesModel
import com.gibsoncodes.filio.models.RecentFilesModel
import kotlinx.coroutines.launch

class RecentFilesViewModel(application: Application,private val  recentFilesProperties: RecentFilesProperties):
    BaseViewModel(application) {
    init {
        loadRecentFilesData()
    }
    private val recentFilesLiveData by lazy {
        MutableLiveData<List<RecentFilesModel>>()
    }
    val liveData:LiveData<List<RecentFilesModel>> get() = recentFilesLiveData
    private fun loadRecentFilesData(){
        viewModelScope.launch {
        val list = recentFilesProperties.invoke()

            recentFilesLiveData.postValue(list.map { it.toRecentFilesModel() })
            if (contentObserver==null){
                contentObserver = getApplication<Application>()
                    .contentResolver.registerContentObserver(MediaStore.Files.getContentUri("external"),
                    onChange = {
                        loadRecentFilesData()
                    })
            }
        }

    }
}