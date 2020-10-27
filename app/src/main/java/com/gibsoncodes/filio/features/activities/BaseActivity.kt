package com.gibsoncodes.filio.features.activities

import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseActivity:AppCompatActivity(){
    protected inline  fun<reified T:ViewDataBinding> bind(resId:Int):Lazy<T> = lazy {
        DataBindingUtil.setContentView<T>(this, resId)
    }
}