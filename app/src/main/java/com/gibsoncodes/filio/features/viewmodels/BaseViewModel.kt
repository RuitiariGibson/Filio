package com.gibsoncodes.filio.features.viewmodels

import android.app.Application
import android.content.ContentResolver
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import androidx.lifecycle.AndroidViewModel

abstract class BaseViewModel(application:Application):AndroidViewModel(application) {
    var contentObserver:ContentObserver?=null
@Suppress("DEPRECATION")
    fun ContentResolver.registerContentObserver(uri:Uri,
    onChange:(Boolean)->Unit):ContentObserver{
        val observer =object: ContentObserver(Handler()){
            override fun onChange(selfChange: Boolean) {
                super.onChange(selfChange)
                 onChange(selfChange)
            }
        }
        registerContentObserver(uri, true, observer)
        return observer
    }

    override fun onCleared() {
        super.onCleared()
       contentObserver?.let{
           getApplication<Application>().contentResolver.unregisterContentObserver(it)
       }
    }
}