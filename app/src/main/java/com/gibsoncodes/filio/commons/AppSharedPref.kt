package com.gibsoncodes.filio.commons

import android.content.Context
import android.content.ContextWrapper
import android.content.SharedPreferences
import com.gibsoncodes.filio.R
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val sharedPrefModule = module {
    single {
        AppSharedPref(androidContext())
    }
}
class AppSharedPref(context:Context):ContextWrapper(context) {
    companion object {
        const val viewedLaunchingScreenKey = "_launchingScreen"
    }

    // private val sharedPreferences =
    private val pref = getSharedPreferences(
        getString(
            R.string.preferences
        ),
        Context.MODE_PRIVATE
    )

    private fun getSharedPreferences(): SharedPreferences {
        return pref
    }

    fun viewedLaunchingScreen(key: String, value: Boolean) {
        val editor = getSharedPreferences().edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun retrieveViewedLaunchingScreenStatus(key: String): Boolean {
        return getSharedPreferences().getBoolean(key, false)
    }


}