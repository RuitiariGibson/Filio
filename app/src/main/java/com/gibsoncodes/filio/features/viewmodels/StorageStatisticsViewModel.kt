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
    init {
        loadStorageStatistics()
    }
    private val storageStatistics by lazy{
        MutableLiveData<StorageStatisticsModel> ()
    }
    val liveData:LiveData<StorageStatisticsModel> get() = storageStatistics
    private fun loadStorageStatistics(){
        viewModelScope.launch {
            var storageStatisticsModel:StorageStatisticsModel?=null

            storageProperties.invoke()?.let {
                storageStatisticsModel=it.toStorageStatisticsModel()
            }
         storageStatistics.postValue(storageStatisticsModel)
        }

    }

}