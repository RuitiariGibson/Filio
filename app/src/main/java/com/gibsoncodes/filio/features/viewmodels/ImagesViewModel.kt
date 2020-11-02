package com.gibsoncodes.filio.features.viewmodels

import android.app.Application
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gibsoncodes.domain.properties.ImagesProperties
import com.gibsoncodes.filio.commons.toImagesModel
import com.gibsoncodes.filio.models.ImagesModel
import kotlinx.coroutines.launch

class ImagesViewModel(application: Application, private val imagesProperties: ImagesProperties):BaseViewModel(application) {
    init{
        loadImages()
    }
private val imagesList by lazy {
    MutableLiveData<List<ImagesModel>> ()
}
    val imagesLiveData:LiveData<List<ImagesModel>> get() = imagesList
   private  fun loadImages(){
        viewModelScope.launch {
            val repoList=imagesProperties.invoke()
            imagesList.postValue(repoList.map{it.toImagesModel()})
            Log.e("images view model tag: ", "total number of images is ${repoList.size}")
            if (contentObserver == null) {
                contentObserver =
                    getApplication<Application>().contentResolver.registerContentObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    ) {
                        loadImages()
                    }
            }
        }

    }
}