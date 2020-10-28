package com.gibsoncodes.filio.features.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gibsoncodes.domain.properties.FileSizeProperties
import com.gibsoncodes.filio.commons.toFileSizeModel
import com.gibsoncodes.filio.models.CategoriesModel
import kotlinx.coroutines.launch

class FileSizesViewModel(private val fileSizeProperties: FileSizeProperties):ViewModel() {
    private val mutableLiveData by lazy{
        MutableLiveData<List<CategoriesModel>>().also {
            it.postValue(loadFileSize())
        }
    }
    val fileSizeLiveData:LiveData<List<CategoriesModel>> get() = mutableLiveData
    private fun loadFileSize():List<CategoriesModel>{
        val category = mutableListOf<CategoriesModel>()
        viewModelScope.launch {
            val fileSizeModel=fileSizeProperties.invoke().toFileSizeModel()
            val (audioSize, videoSize, downloadSize, imageSize) =fileSizeModel
            val categoriesList = arrayListOf(
                CategoriesModel(categoryName = "Downloads",categorySize = downloadSize),
                CategoriesModel(categoryName = "Images",categorySize = imageSize),
                CategoriesModel(categoryName = "Videos",categorySize = videoSize),
                CategoriesModel(categoryName = "Audio",categorySize = audioSize)
            )
            category.addAll(categoriesList)
        }

        return category
    }
}