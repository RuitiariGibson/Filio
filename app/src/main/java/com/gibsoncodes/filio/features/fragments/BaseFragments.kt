package com.gibsoncodes.filio.features.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class BaseFragments:Fragment() {

    inline fun<reified T:ViewDataBinding> bindFragment(parent:ViewGroup?, inflater:LayoutInflater,
                                                       @LayoutRes layoutId:Int,
    attachToRoot:Boolean)
     :T =
        DataBindingUtil.inflate<T>(inflater,
        layoutId, parent, attachToRoot)

}