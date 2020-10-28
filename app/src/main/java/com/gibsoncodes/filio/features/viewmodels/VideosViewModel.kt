package com.gibsoncodes.filio.features.viewmodels

import android.app.Application
import android.provider.MediaStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gibsoncodes.domain.properties.VideosProperties
import com.gibsoncodes.filio.commons.toVideosModel
import com.gibsoncodes.filio.models.VideosModel
import kotlinx.coroutines.launch

class VideosViewModel(application: Application, private val videosProperties: VideosProperties):BaseViewModel(application) {
    private val videosList by lazy {
        MutableLiveData<List<VideosModel>>()
    }
    init {
        loadVideos()
    }
    val videosLiveData: LiveData<List<VideosModel>> get() = videosList
    private fun loadVideos() {
        val list = mutableListOf<VideosModel>()
        viewModelScope.launch {
            val repoList = videosProperties.invoke()
            list.addAll(repoList.map { it.toVideosModel() })
            videosList.postValue(list)
            if (contentObserver == null) {
                contentObserver =
                    getApplication<Application>().contentResolver.registerContentObserver(
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    ) {
                        loadVideos()
                    }
            }
        }
    }

}