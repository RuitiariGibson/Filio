package com.gibsoncodes.filio.commons

import android.animation.Animator

class AnimatorListener (private val onEnd:(Animator?)->Unit):Animator.AnimatorListener {
    override fun onAnimationRepeat(p0: Animator?) {

    }

    override fun onAnimationEnd(p0: Animator?) {
       onEnd(p0)
    }

    override fun onAnimationCancel(p0: Animator?) {

    }

    override fun onAnimationStart(p0: Animator?) {
    }

}