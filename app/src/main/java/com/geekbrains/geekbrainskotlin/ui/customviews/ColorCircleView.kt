package com.geekbrains.geekbrainskotlin.ui.customviews

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.support.annotation.ColorRes
import android.support.annotation.Dimension
import android.support.annotation.Dimension.DP
import android.support.annotation.Dimension.PX
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import com.geekbrains.geekbrainskotlin.R
import org.jetbrains.anko.colorAttr
import org.jetbrains.anko.dip

@Dimension(unit = DP) private const val defRadiusDp = 16
@Dimension(unit = DP) private const val defStrokeWidthDp = 1

class ColorCircleView @JvmOverloads constructor(context: Context,
                                           attrs: AttributeSet? = null,
                                           defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    private val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
    }

    private var center: Pair<Float, Float> = 0f to 0f

    @Dimension(unit = PX) var radius: Float

    @ColorRes var fillColorRes: Int = R.color.color_white
        set(value) {
            field = value
            fillPaint.color = ContextCompat.getColor(context, value)
        }

    @ColorRes var strokeColorRes: Int = R.color.color_text_secondary
        set(value) {
            field = value
            strokePaint.color = ContextCompat.getColor(context, value)
        }

    @Dimension(unit = PX) var strokeWidth: Float = dip(defStrokeWidthDp).toFloat()
        set(value) {
            field = value
            strokePaint.strokeWidth = value
        }

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.ColorCircleView)

        val defRadiusPx = dip(defRadiusDp).toFloat()
        radius = a.getDimension(R.styleable.ColorCircleView_circleRadius, defRadiusPx)
        fillColorRes = a.getResourceId(
                R.styleable.ColorCircleView_fillColor, R.color.color_white)

        val defStrokeWidthPx = dip(defStrokeWidthDp).toFloat()
        strokeWidth = a.getDimension(R.styleable.ColorCircleView_strokeWidth, defStrokeWidthPx)
        strokeColorRes = a.getResourceId(
                R.styleable.ColorCircleView_strokeColor, R.color.color_text_secondary)
        a.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val height = (radius * 2 + paddingTop + paddingBottom).toInt()
        val width = (radius * 2 + paddingStart + paddingEnd).toInt()

        setMeasuredDimension(width, height)
    }


    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        center = measuredWidth/2f to measuredHeight/2f
        super.onLayout(changed, left, top, right, bottom)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawCircle(center.first, center.second, radius, strokePaint)
        canvas.drawCircle(center.first, center.second, radius - strokeWidth, fillPaint)
    }
}