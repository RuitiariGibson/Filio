package com.gibsoncodes.filio.features.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gibsoncodes.filio.R
import com.gibsoncodes.filio.commons.AppSharedPref
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen2)
        val preferences: AppSharedPref by inject()
        val mainThreadJob = MainScope()
        mainThreadJob.launch {
            delay(4000L)
            val viewedLaunchScreen =
                preferences.retrieveViewedLaunchingScreenStatus(AppSharedPref.viewedLaunchingScreenKey)
            if (viewedLaunchScreen) {
                startActivity(Intent(this@SplashScreen, MainActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this@SplashScreen, LauncherActivity::class.java))
                finish()

            }
        }

    }
}