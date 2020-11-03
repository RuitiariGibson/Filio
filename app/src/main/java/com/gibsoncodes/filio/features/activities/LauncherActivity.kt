package com.gibsoncodes.filio.features.activities

import android.content.Intent
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import com.gibsoncodes.filio.commons.AppSharedPref
import com.gibsoncodes.filio.commons.fadeIn
import com.gibsoncodes.filio.databinding.ActivitySplashScreenBinding
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import org.koin.android.ext.android.inject

class LauncherActivity : AppCompatActivity() {
    private val defaultFadeDuration = 2500L
    private val appPreferences: AppSharedPref by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        val binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setExitSharedElementCallback(MaterialContainerTransformSharedElementCallback())
        window.sharedElementsUseOverlay = false
        setContentView(binding.root)
        super.onCreate(savedInstanceState)
        binding.manageYourFilesText.fadeIn(fadeDuration = defaultFadeDuration)
        binding.appSellText.fadeIn(
            halfTime = 2 * defaultFadeDuration,
            fadeDuration = defaultFadeDuration
        )
        binding.bottomLayout.fadeIn(
            halfTime = 2 * defaultFadeDuration,
            fadeDuration = defaultFadeDuration
        )
        binding.lastText.fadeIn(
            halfTime = 3 * defaultFadeDuration,
            fadeDuration = defaultFadeDuration
        )
        binding.magicHappen.fadeIn(
            halfTime = 3 * defaultFadeDuration,
            fadeDuration = defaultFadeDuration
        )
        binding.proceedButton.setOnClickListener {
            appPreferences.viewedLaunchingScreen(
                AppSharedPref.viewedLaunchingScreenKey,
                true
            )
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this, binding.proceedButton, "shared_element_end_root"
            )
            Intent(this, MainActivity::class.java).also {
                startActivity(it, options.toBundle())
            }
            finish()
        }

    }
}