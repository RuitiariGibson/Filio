package com.gibsoncodes.filio.features.viewmodels

import android.app.Application
import android.provider.MediaStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gibsoncodes.domain.properties.DownloadsProperty
import com.gibsoncodes.filio.commons.toDownloadsModel
import com.gibsoncodes.filio.models.DownloadsModel
import kotlinx.coroutines.launch

class DownloadsViewModel(application: Application,
                         private val downloadsProperty: DownloadsProperty):BaseViewModel(application) {

    private val downloadFilesList by lazy{
        MutableLiveData<List<DownloadsModel>> ()
            .also {
                loadDownloadFiles()
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