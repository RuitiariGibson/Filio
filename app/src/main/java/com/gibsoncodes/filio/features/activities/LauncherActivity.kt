package com.gibsoncodes.filio.features.activities

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.DecelerateInterpolator
import com.gibsoncodes.filio.R
import com.gibsoncodes.filio.commons.AnimatorListener
import com.gibsoncodes.filio.databinding.ActivitySplashScreenBinding

class LauncherActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding:ActivitySplashScreenBinding by bind(R.layout.activity_splash_screen)

        with(binding.manageYourFilesText)
        {
            this.animate()
                .translationY(-this.height.toFloat())
                .alpha(1f)
                .setDuration(3000L)
                .setInterpolator(DecelerateInterpolator(0.5f))
                .setListener(AnimatorListener{
                    this.visibility=View.GONE
                })

        }

        with(binding.bottomLayout){

            this.animate()
                .translationY(-this.height.toFloat())
                .alpha(1f)
                .setDuration(1000L)
                .setInterpolator(DecelerateInterpolator(0.5f))
                .setListener(AnimatorListener{
                    this.visibility=View.GONE
                })
        }

        /**
         * Create a glowing effect by fading it and out slowly
         * Although we can use  view property animation since it invalidates itself
         * A view property animation is meant for simpler tasks
         * So we use an objectAnimator instead
         */
        val objectAnimator = ObjectAnimator.ofFloat(binding.glowingImageView,
        "alpha",0f,1f)
        objectAnimator.apply {
            this.duration=2000L
            this.interpolator=DecelerateInterpolator(0.5f) // slow down the animation by half the time
            this.repeatCount=1
            this.repeatMode=ValueAnimator.REVERSE // reverse it to complete the effect
        }
        objectAnimator.start()
       binding.proceedButton.setOnClickListener { Intent(this,MainActivity::class.java).also { startActivity(it) } }

    }
}