package com.gibsoncodes.filio.commons

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.doOnPreDraw
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import com.gibsoncodes.filio.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlin.math.abs
// authored by Patrickiv (Medium)
private const val DEFAULT_SCALE = 1f
private const val MAX_SCALE = 15f
private const val BASE_DURATION = 300L
private const val VARIABLE_DURATION = 300L

class BottomNavigationIndicator:BottomNavigationView,
BottomNavigationView.OnNavigationItemSelectedListener{
    private var externalSelectedListener:OnNavigationItemSelectedListener ? =null
    private var animator:ValueAnimator?=null
    private val indicator = RectF()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply{
        color = ContextCompat.getColor(context,
            R.color.colorAccent
        )
    }
    private val bottomOffset = resources.getDimension(R.dimen.bottom_margin)
    private val defaultSize = resources.getDimension(R.dimen.default_size)
    constructor(context:Context):super(context)
    constructor(context:Context, attrs:AttributeSet?):super(context, attrs)
    init{
        super.setOnNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (externalSelectedListener?.onNavigationItemSelected(item)!=false){
            return true
        }
        return false
    }
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        doOnPreDraw {
            // does not have on draw thus we use doOnPreDraw
            // Move the indicator in place when the view is laid out
            onItemSelected(selectedItemId, false)
        }
    }
    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        if (isLaidOut) {
            val cornerRadius = indicator.height() / 2f
            canvas.drawRoundRect(indicator, cornerRadius, cornerRadius, paint)
        }
    }
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        // Clean up the animator if the view is going away
        cancelAnimation( true)
    }
    private fun onItemSelected(itemId: Int, animate: Boolean = true) {
        if (!isLaidOut) return

        // Interrupt any current animation, but don't set the end values,
        // if it's in the middle of a movement we want it to start from
        // the current position, to make the transition smoother.
        cancelAnimation(endValues = false)

        val itemView = findViewById<View>(itemId) ?: return
        val fromCenterX = indicator.centerX()
        val fromScale = indicator.width() / defaultSize


        animator = ValueAnimator.ofFloat(fromScale,
            MAX_SCALE,
            DEFAULT_SCALE
        ).apply {
            addUpdateListener {
                val progress = it.animatedFraction
                val distanceTravelled = linearInterpolation(progress, fromCenterX, itemView.centerX)

                val scale = it.animatedValue as Float
                val indicatorWidth = defaultSize * scale

                val left = distanceTravelled - indicatorWidth / 2f
                val top = height - bottomOffset - defaultSize
                val right = distanceTravelled + indicatorWidth / 2f
                val bottom = height - bottomOffset

                indicator.set(left, top, right, bottom)
                invalidate()
            }

            interpolator = LinearOutSlowInInterpolator()

            val distanceToMove = abs(fromCenterX - itemView.centerX)
            duration = if (animate) calculateDuration(distanceToMove) else 0L

            start()
        }
    }
    /**
     * Speed up the animation using
     * speed up the transition from one frame to another
     * a and b are equiavlent to x and y with t being the constant
     * Linear interpolation between 'a' and 'b' based on the progress 't'
     */
    private fun linearInterpolation(t:Float, a:Float, b:Float) =(1-1)*a+t*b
    override fun setOnNavigationItemSelectedListener(listener: OnNavigationItemSelectedListener?) {
        externalSelectedListener=listener
    }


    /**
     * Calculates a duration for the translation based on a fixed duration + a duration
     * based on the distance the indicator is being moved.
     */
    private fun calculateDuration(distance:Float)=
        ((BASE_DURATION + VARIABLE_DURATION) * (distance/width).coerceIn(0f,1f)).toLong()
    private val View.centerX get() = left+width/2f
    private fun cancelAnimation(endValues:Boolean) = animator?.let {
        if(endValues){
            it.end()
        }else it.cancel()
        it.removeAllUpdateListeners()
        animator=null
    }
}