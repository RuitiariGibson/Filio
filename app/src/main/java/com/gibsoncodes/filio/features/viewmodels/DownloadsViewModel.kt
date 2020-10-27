package com.gibsoncodes.filio.features.viewmodels

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.MediaStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gibsoncodes.domain.properties.DownloadsProperty
import com.gibsoncodes.filio.R
import com.gibsoncodes.filio.commons.loadThumbnail
import com.gibsoncodes.filio.commons.toDownloadsModel
import com.gibsoncodes.filio.models.DownloadsModel
import kotlinx.coroutines.launch

class DownloadsViewModel(application: Application,
                         private val downloadsProperty: DownloadsProperty):BaseViewModel(application) {

    private val downloadFilesList by lazy{
        MutableLiveData<List<DownloadsModel>> ()
            .also {
               val list= loadDownloadFiles()
               list.map {
                   model->
                  if (model.bitmap==null){
                      model.bitmap=getApplication<Application>().loadThumbnail(model.fileExtension)
                  }
               }
                it.postValue(list)
            }
    }
    val downloadFilesLiveData:LiveData<List<DownloadsModel>> get() = downloadFilesList
  private  fun loadDownloadFiles():List<DownloadsModel> {
      val returnList = mutableListOf<DownloadsModel>()
      viewModelScope.launch {
          returnList.addAll(downloadsProperty.invoke().map { it.toDownloadsModel() })
          if (contentObserver == null) {
              contentObserver =
                  getApplication<Application>().contentResolver.registerContentObserver(
                      MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                  ) {
                      loadDownloadFiles()
                  }
          }
      }
      return returnList
  }


}