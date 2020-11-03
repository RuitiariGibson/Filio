package com.gibsoncodes.filio.features.viewmodels

import android.app.Application
import android.app.RecoverableSecurityException
import android.content.IntentSender
import android.os.Build
import android.provider.MediaStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gibsoncodes.domain.properties.AudiosProperties
import com.gibsoncodes.filio.commons.loadThumbnail
import com.gibsoncodes.filio.commons.toAudiosModel
import com.gibsoncodes.filio.models.AudiosModel
import com.gibsoncodes.filio.models.VideosModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AudioViewModel(application: Application,
private val properties:AudiosProperties):BaseViewModel(application) {

    private val audiosLiveData  by lazy{
        MutableLiveData<List<AudiosModel>> ()
    }


    init {
        loadAudios()
    }
    val audioData:LiveData<List<AudiosModel>> get() = audiosLiveData
    private fun loadAudios():List<AudiosModel>{
        val list = mutableListOf<AudiosModel>()
        viewModelScope.launch {
            list.addAll(properties.invoke().map { it.toAudiosModel() })
            list.map {
                    audio->
                if (audio.thumbnail==null){
                    audio.thumbnail = getApplication<Application>().loadThumbnail("mp3")
                }
            }
            audiosLiveData.postValue(list)
            if (contentObserver == null) {
                contentObserver = getApplication<Application>().contentResolver.registerContentObserver(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    ) {
                        loadAudios()
                    }
            }
        }
        return list
    }


}