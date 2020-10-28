package com.gibsoncodes.filio.commons

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.gibsoncodes.filio.R
import kotlin.math.roundToInt

class CircleProgressBar(context:Context,attrs:AttributeSet):View(context,attrs) {
    /**
     * ProgressBar's line thickness
     */
    private var strokeWidth = 4f
    private var progress = 0f
    private var min = 0
    private var max = 100
    private var centerX=0
    private var centerY=0
    private val rect = Rect()


    /**
     * Start the progress at 12 o'clock
     */
    private val startAngle = -90
    private var color: Int = Color.DKGRAY
    private var rectF: RectF? = null
    private var backgroundPaint: Paint? = null
    private var foregroundPaint: Paint? = null
    private var textPaint:Paint?=null
    fun getStrokeWidth(): Float {
        return strokeWidth
    }

    fun setStrokeWidth(strokeWidth: Float) {
        this.strokeWidth = strokeWidth
        backgroundPaint?.strokeWidth = strokeWidth
        foregroundPaint?.strokeWidth = strokeWidth
        textPaint?.strokeWidth = 2f
        invalidate()
        requestLayout() //Because it should recalculate its bounds
    }

    fun getProgress(): Float {
        return progress
    }

    fun setProgress(progress: Float) {
        this.progress = progress
        invalidate()
    }

    fun getMin(): Int {
        return min
    }

    fun setMin(min: Int) {
        this.min = min
        invalidate()
    }

    fun getMax(): Int {
        return max
    }

    fun setMax(max: Int) {
        this.max = max
        invalidate()
    }

    fun getColor(): Int {
        return color
    }

    fun setColor(color: Int) {
        this.color = color
        backgroundPaint?.color=adjustAlpha(color, 0.3f)
        foregroundPaint?.color = color
        invalidate()
        requestLayout()
    }

    private fun init(context: Context, attrs: AttributeSet) {
        rectF = RectF()
        val typedArray: TypedArray = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CircleProgressBar,
            0, 0
        )
        //Reading values from the XML layout
        try {
            strokeWidth = typedArray.getDimension(
                R.styleable.CircleProgressBar_progressBarThickness,
                strokeWidth
            )
            progress = typedArray.getFloat(R.styleable.CircleProgressBar_progress, progress)
            color = typedArray.getInt(R.styleable.CircleProgressBar_progressbarColor, color)
            min = typedArray.getInt(R.styleable.CircleProgressBar_min, min)
            max = typedArray.getInt(R.styleable.CircleProgressBar_max, max)
        } finally {
            typedArray.recycle()
        }
        backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        backgroundPaint!!.color = adjustAlpha(color, 0.3f)
        backgroundPaint!!.style = Paint.Style.STROKE
        backgroundPaint!!.strokeWidth = strokeWidth
        foregroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        foregroundPaint!!.color = color
        foregroundPaint!!.style = Paint.Style.STROKE
        foregroundPaint!!.strokeWidth = strokeWidth
        textPaint= Paint(Paint.ANTI_ALIAS_FLAG)
        textPaint!!.style= Paint.Style.FILL
        textPaint!!.color = Color.WHITE
        textPaint!!.strokeWidth=0.5f

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        backgroundPaint?.let { rectF?.let { it1 -> canvas.drawOval(it1, it) } }
        val angle = 360 * progress / max
        rectF?.let { canvas.drawArc(it, startAngle.toFloat(), angle, false, foregroundPaint!!) }
        val text = " $progress% "
        textPaint?.getTextBounds(text,0,text.length,rect)
        canvas.drawText(text,centerX-rect.exactCenterX(),centerY-rect.exactCenterY(),
            textPaint!!)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val height =
            getDefaultSize(suggestedMinimumHeight, heightMeasureSpec)
        val width =
            getDefaultSize(suggestedMinimumWidth, widthMeasureSpec)
        val min = width.coerceAtMost(height)
        centerX=width/2
        centerY=height/2
        setMeasuredDimension(min, min)
        rectF!![0 + strokeWidth / 2, 0 + strokeWidth / 2, min - strokeWidth / 2] =
            min - strokeWidth / 2
        textPaint?.textSize=min*0.1f
    }

    /**
     * Lighten the given color by the factor
     *
     * @param color  The color to lighten
     * @param factor 0 to 4
     * @return A brighter color
     */
    fun lightenColor(color: Int, factor: Float): Int {
        val r: Float = Color.red(color) * factor
        val g: Float = Color.green(color) * factor
        val b: Float = Color.blue(color) * factor
        val ir = Math.min(255, r.toInt())
        val ig = Math.min(255, g.toInt())
        val ib = Math.min(255, b.toInt())
        val ia: Int = Color.alpha(color)
        return Color.argb(ia, ir, ig, ib)
    }

    /**
     * Transparent the given color by the factor
     * The more the factor closer to zero the more the color gets transparent
     *
     * @param color  The color to transparent
     * @param factor 1.0f to 0.0f
     * @return int - A transplanted color
     */
   private fun adjustAlpha(color: Int, factor: Float): Int {
        val alpha = (Color.alpha(color) * factor).roundToInt().toInt()
        val red: Int = Color.red(color)
        val green: Int = Color.green(color)
        val blue: Int = Color.blue(color)
        return Color.argb(alpha, red, green, blue)
    }



    init {
        init(context, attrs)
    }
}