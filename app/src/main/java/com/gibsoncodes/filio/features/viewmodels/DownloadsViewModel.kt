package com.gibsoncodes.filio.features.viewmodels

import android.app.Application
import android.app.RecoverableSecurityException
import android.content.IntentSender
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.net.toFile
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gibsoncodes.domain.properties.DownloadsProperty
import com.gibsoncodes.filio.R
import com.gibsoncodes.filio.commons.loadThumbnail
import com.gibsoncodes.filio.commons.toDownloadsModel
import com.gibsoncodes.filio.models.DownloadsModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.handleCoroutineException
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException

class DownloadsViewModel(application: Application,
                         private val downloadsProperty: DownloadsProperty):BaseViewModel(application) {

    private val downloadFilesList by lazy{
        MutableLiveData<List<DownloadsModel>> ()

    }
    init {
        loadDownloadFiles()
    }
    val downloadFilesLiveData:LiveData<List<DownloadsModel>> get() = downloadFilesList
  private  fun loadDownloadFiles():List<DownloadsModel> {
      val returnList = mutableListOf<DownloadsModel>()
      viewModelScope.launch {
          returnList.addAll(downloadsProperty.invoke().map { it.toDownloadsModel() })
          returnList.map{
                  model->
              if (model.bitmap==null){
                  model.bitmap=getApplication<Application>().loadThumbnail(model.fileExtension)
              }
          }
          downloadFilesList.postValue(returnList)
          if (contentObserver == null) {
              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                  contentObserver =
                      getApplication<Application>().contentResolver.registerContentObserver(
                          MediaStore.Downloads.EXTERNAL_CONTENT_URI
                      ) {
                          loadDownloadFiles()
                      }
              }
          }
      }
      return returnList
  }
private var downloadModel:DownloadsModel?=null
    private val _permissionNeededToDelete = MutableLiveData<IntentSender?> ()
    val permissionNeededToDelete:LiveData<IntentSender?> get() = _permissionNeededToDelete

    fun deleteDownloadFile(downloadsModel: DownloadsModel){
    viewModelScope.launch {
        deleteFunctionForDownloads(downloadsModel)
    }
    }

    fun deletePendingDownloadFile(){
        downloadModel?.let{
            downloadModel=null
            deleteDownloadFile(it)
        }
    }
    private suspend fun deleteFunctionForDownloads(downloadsModel: DownloadsModel){
        withContext(Dispatchers.IO){
            try{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                    getApplication<Application>().contentResolver.delete(downloadsModel.uri,
                        "${MediaStore.DownloadColumns.TITLE} = ?", arrayOf(downloadsModel.fileName))
                }else{
                  val file = downloadsModel.uri.toFile()
                    try{
                        file.delete()
                    }catch (io:IOException){
                        Log.e("Downlaods view model","error deleting the file")
                    }
                }
            }catch (ex:SecurityException){
                val exception:RecoverableSecurityException = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    ex as? RecoverableSecurityException ?: throw ex

                } else {
                    throw ex
                }
                downloadModel= downloadsModel
                _permissionNeededToDelete.postValue(exception.userAction.actionIntent.intentSender)

            }
        }
    }

}