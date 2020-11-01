package com.gibsoncodes.filio.features.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AlphaAnimation
import androidx.appcompat.widget.AppCompatImageView
import com.gibsoncodes.filio.R
import com.gibsoncodes.filio.commons.fadeIn
import com.gibsoncodes.filio.databinding.ActivitySplashScreenBinding

class LauncherActivity : BaseActivity() {

    private val defaultFadeDuration =2500L

    private fun AppCompatImageView.animateImage(){
        this.visibility = View.INVISIBLE
        Handler().postDelayed({
            this.animate()
                .alpha(1f)
                .setDuration(100L)
                .withEndAction {
                   this.visibility=View.VISIBLE
                    animateImage()
                }
        }, 2*100L)

    }
  private  val binding:ActivitySplashScreenBinding by bind(R.layout.activity_splash_screen)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.manageYourFilesText.fadeIn(fadeDuration = defaultFadeDuration)
        binding.appSellText.fadeIn(halfTime = 2*defaultFadeDuration, fadeDuration = defaultFadeDuration)
       binding.bottomLayout.fadeIn(halfTime = 2*defaultFadeDuration, fadeDuration = defaultFadeDuration)
        binding.lastText.fadeIn(halfTime = 3*defaultFadeDuration, fadeDuration = defaultFadeDuration)
        binding.magicHappen.fadeIn(halfTime = 3*defaultFadeDuration,fadeDuration = defaultFadeDuration)
        binding.glowingImageView.animateImage()



       binding.proceedButton.setOnClickListener { Intent(this,MainActivity::class.java).also { startActivity(it) } }

    }
}