package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0

    private var progress = 0f

    private var buttonText = "DOWNLOAD"

    private val valueAnimator = ValueAnimator()

    private val textPaint = Paint().apply {
        style = Paint.Style.FILL
        color = Color.WHITE
        textAlign = Paint.Align.CENTER
        textSize = 50.0f
    }

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        when (new) {
            ButtonState.Loading -> {
                setButtonText(resources.getString(R.string.button_loading))
            }
            ButtonState.Completed -> {

            }
            ButtonState.Clicked -> {
                // no-op
            }
        }
    }


    init {
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawText(buttonText, width / 2.0f, height / 1.8f, textPaint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

    private fun resetProgress() {
        progress = 0f
    }

    private fun setButtonText(text: String) {
        this.buttonText = text
        invalidate()
        requestLayout()
    }

    fun setLoadingButtonState(state: ButtonState) {
        buttonState = state
    }

}