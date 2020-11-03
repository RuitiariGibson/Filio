package com.gibsoncodes.filio.features.viewmodels

import android.app.Application
import android.app.RecoverableSecurityException
import android.content.IntentSender
import android.os.Build
import android.provider.MediaStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gibsoncodes.domain.properties.VideosProperties
import com.gibsoncodes.filio.commons.toVideosModel
import com.gibsoncodes.filio.models.VideosModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VideosViewModel(application: Application, private val videosProperties: VideosProperties):BaseViewModel(application) {
    private val videosList by lazy {
        MutableLiveData<List<VideosModel>>()
    }
    init {
        loadVideos()
    }
    private val _permissionNeededToDelete = MutableLiveData<IntentSender?> ()
    val permissionNeededToDelete:LiveData<IntentSender?> get()= _permissionNeededToDelete
    private var videoToBeDeleted:VideosModel?=null
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
    fun deleteVideo(videosModel: VideosModel){
        viewModelScope.launch{
            deleteVideoFunction(videosModel)
        }
    }
    fun deletePendingVideoModel(){
        videoToBeDeleted?.let{
                video->
            videoToBeDeleted=null
            deleteVideo(video)
        }
    }
    private suspend fun deleteVideoFunction(videosModel: VideosModel){
        withContext(Dispatchers.IO){
            try{
                videosModel.uri?.let{
                    getApplication<Application>().contentResolver.delete(videosModel.uri,
                        "${MediaStore.Video.Media._ID} = ?",
                        arrayOf(videosModel.id.toString()))
                }

            }catch (ex:SecurityException) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    val exception: RecoverableSecurityException =
                        ex as? RecoverableSecurityException ?: throw ex
                    videoToBeDeleted = videosModel
                    _permissionNeededToDelete.postValue(
                        exception.userAction.actionIntent.intentSender
                    )
                }else{
                    throw ex
                }
            }
        }
    }
}