package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.content.withStyledAttributes
import kotlinx.android.synthetic.main.content_main.view.*
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private var progressWidth = 0f
    private var buttonText = "DOWNLOAD"
    private var valueAnimator = ValueAnimator()

    private var progressColor = 0
    private var buttonColor = 0
    private var loadingCircleColor = 0

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.LoadingButton, 0, 0).apply {
            progressColor = getColor(R.styleable.LoadingButton_progressColor, 0)
            buttonColor = getColor(R.styleable.LoadingButton_buttonColor, 0)
            loadingCircleColor = getColor(R.styleable.LoadingButton_loadingCircleColor, 0)
        }
    }

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
                valueAnimator = ValueAnimator.ofFloat(0f, measuredWidth.toFloat()).apply {
                    duration = 4000
                    addUpdateListener {
                        progressWidth = animatedValue as Float
                        invalidate()
                    }
                    start()
                }
                custom_button.isEnabled = false
            }
            ButtonState.Completed -> {
                valueAnimator.cancel()
                resetProgress()
                custom_button.isEnabled = true
            }
            ButtonState.Clicked -> {
                // no-op
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val progressPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            color = progressColor
        }
        canvas.drawColor(buttonColor)
        if (buttonState === ButtonState.Loading) {
            canvas.drawRect(0.0f, 0.0f, progressWidth, heightSize.toFloat(), progressPaint)

        }
        canvas.drawText(
            buttonText,
            widthSize.toFloat() / 2.0f,
            heightSize.toFloat() / 1.8f,
            textPaint
        )
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
        progressWidth = 0f
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