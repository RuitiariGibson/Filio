package com.gibsoncodes.filio.features.viewmodels

import android.app.Application
import android.provider.MediaStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gibsoncodes.domain.properties.ImagesProperties
import com.gibsoncodes.filio.commons.toImagesModel
import com.gibsoncodes.filio.models.ImagesModel
import kotlinx.coroutines.launch

class ImagesViewModel(application: Application, private val imagesProperties: ImagesProperties):BaseViewModel(application) {
private val imagesList by lazy {
    MutableLiveData<List<ImagesModel>> ().also {
        val imagesData = loadImages()
        it.postValue(imagesData)
    }
}
    val imagesLiveData:LiveData<List<ImagesModel>> get() = imagesList
   private  fun loadImages():List<ImagesModel> {
       val list = mutableListOf<ImagesModel>()
        viewModelScope.launch {
            val repoList=imagesProperties.invoke()
            list.addAll(repoList.map { it.toImagesModel() })
            if (contentObserver == null) {
                contentObserver =
                    getApplication<Application>().contentResolver.registerContentObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    ) {
                        loadImages()
                    }
            }
        }
       return list
    }
}