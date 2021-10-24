package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlinx.android.synthetic.main.content_main.view.*
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0

    private var progress = 0.0f

    private var buttonText = "Download"
    private var valueAnimator = ValueAnimator()

    private val textArea = Rect()

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

    private val progressBarPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = progressColor
    }

    private val loadingCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = loadingCircleColor
    }

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        when (new) {
            ButtonState.Loading -> {
                setButtonText(resources.getString(R.string.button_loading))
                valueAnimator = ValueAnimator.ofFloat(0.0f, 1.0f).apply {
                    duration = 2000
                    repeatCount = ValueAnimator.INFINITE
                    repeatMode = ValueAnimator.RESTART
                    addUpdateListener {
                        progress = animatedValue as Float
                        invalidate()
                    }
                    start()
                }
                custom_button.isEnabled = false
            }
            ButtonState.Completed -> {
                valueAnimator.cancel()
                resetProgress()
                setButtonText("Download")
                custom_button.isEnabled = true
            }
            ButtonState.Clicked -> {
                // no-op
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        textPaint.getTextBounds(buttonText, 0, buttonText.length, textArea)

        canvas.drawColor(buttonColor)

        if (buttonState === ButtonState.Loading) {
            val progressWidth = progress * widthSize.toFloat()
            canvas.drawRect(0.0f, 0.0f, progressWidth, heightSize.toFloat(), progressBarPaint)

            val progressCircle = progress * 360f
            canvas.drawArc(
                widthSize / 2f + textArea.width() / 2,
                heightSize / 2f - 35f,
                widthSize / 2 + 70f + textArea.width() / 2,
                heightSize / 2f + 35f,
                0f,
                progressCircle,
                true,
                loadingCirclePaint
            )
        }
        canvas.drawText(
            buttonText,
            widthSize.toFloat() / 2.0f,
            heightSize.toFloat() / 1.7f,
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
        progress = 0.0f
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