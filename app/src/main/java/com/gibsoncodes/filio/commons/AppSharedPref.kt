package com.gibsoncodes.filio.commons

import android.content.Context
import android.content.ContextWrapper
import android.content.SharedPreferences
import com.gibsoncodes.filio.R

class AppSharedPref(context:Context):ContextWrapper(context) {

   // private val sharedPreferences =
    private val pref = getSharedPreferences(getString(
       R.string.preferences),
   Context.MODE_PRIVATE)


}