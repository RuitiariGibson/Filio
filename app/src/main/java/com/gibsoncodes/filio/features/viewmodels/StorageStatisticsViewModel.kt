package com.gibsoncodes.filio.features.viewmodels


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gibsoncodes.domain.properties.StatisticsProperty
import com.gibsoncodes.filio.commons.toStorageStatisticsModel
import com.gibsoncodes.filio.models.StorageStatisticsModel
import kotlinx.coroutines.launch

class StorageStatisticsViewModel(private val storageProperties:StatisticsProperty):ViewModel() {
    private val storageStatistics by lazy{
        MutableLiveData<StorageStatisticsModel> ().also {
            val data = loadStorageStatistics()
            it.postValue(data)
        }
    }
    val liveData:LiveData<StorageStatisticsModel> get() = storageStatistics
    private fun loadStorageStatistics():StorageStatisticsModel?{
        var storageStatisticsModel:StorageStatisticsModel?=null
        viewModelScope.launch {
            storageProperties.invoke().let {
                storageStatisticsModel=it?.toStorageStatisticsModel()
            }

        }
    return storageStatisticsModel
    }

}