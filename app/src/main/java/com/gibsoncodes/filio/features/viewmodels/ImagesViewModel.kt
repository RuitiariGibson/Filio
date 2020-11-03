package com.gibsoncodes.filio.features.viewmodels

import android.app.Application
import android.app.RecoverableSecurityException
import android.content.IntentSender
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gibsoncodes.domain.properties.ImagesProperties
import com.gibsoncodes.filio.commons.toImagesModel
import com.gibsoncodes.filio.models.ImagesModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ImagesViewModel(application: Application, private val imagesProperties: ImagesProperties):BaseViewModel(application) {
    init{
        loadImages()
    }
private val imagesList by lazy {
    MutableLiveData<List<ImagesModel>> ()
}
    private var pendingImageToBeDeleted :ImagesModel? =null
    private val _permissionNeededToDelete = MutableLiveData<IntentSender?> ()
    val permissionNeededToDelete:LiveData<IntentSender?>  get() = _permissionNeededToDelete
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
    fun deleteImage(image:ImagesModel){
        viewModelScope.launch {
            performDeleteImageFunction(image)
        }
    }
    fun deletePendingImage(){
        pendingImageToBeDeleted?.let{
            image ->
            pendingImageToBeDeleted=null
            deleteImage(image)
        }
    }
    private suspend fun performDeleteImageFunction(image:ImagesModel) {
        withContext(Dispatchers.IO) {
            try {
                getApplication<Application>().contentResolver.delete(
                    image.uri!!,
                    "${MediaStore.Images.Media.TITLE} = ?", arrayOf(image.name)
                )
            } catch (ex: SecurityException) {
                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q){
                    val recoverableSecurityException:RecoverableSecurityException
                    = ex as? RecoverableSecurityException ?: throw ex
                    pendingImageToBeDeleted = image
                    _permissionNeededToDelete.postValue(
                        recoverableSecurityException.userAction.actionIntent.intentSender)
                }else{
                    throw ex
                }
            }
        }
    }
}