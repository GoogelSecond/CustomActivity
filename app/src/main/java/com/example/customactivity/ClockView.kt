package com.example.customactivity

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import kotlin.properties.Delegates

class ClockView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = R.attr.clockStyle,
    defStyleRes: Int = R.style.DefaultClockStyle
) : View(context, attributeSet, defStyleAttr, defStyleRes) {

    var timeModel: TimeModel = TimeModel()
        set(value) {
            field = value
            invalidate()
        }

    private var hourHandColor by Delegates.notNull<Int>()
    private var minuteHandColor by Delegates.notNull<Int>()
    private var secondHandColor by Delegates.notNull<Int>()

    private var hourHandLength by Delegates.notNull<Float>()
    private var minuteHandLength by Delegates.notNull<Float>()
    private var secondHandLength by Delegates.notNull<Float>()

    private lateinit var clockPaint: Paint
    private lateinit var hourHandPaint: Paint
    private lateinit var minuteHandPaint: Paint
    private lateinit var secondHandPaint: Paint

    private var centerX by Delegates.notNull<Float>()
    private var centerY by Delegates.notNull<Float>()
    private var radius by Delegates.notNull<Float>()

    init {
        if (attributeSet != null) {
            initAttributeSet(attributeSet, defStyleAttr, defStyleRes)
        } else {
            initDefault()
        }

        initPaints()

        if (isInEditMode) {
            timeModel = TimeModel(6f, 55f, 1f)
        }
    }

    private fun initAttributeSet(attributeSet: AttributeSet, defStyleAttr: Int, defStyleRes: Int) {
        val typedArray = context.obtainStyledAttributes(
            attributeSet,
            R.styleable.ClockView,
            defStyleAttr,
            defStyleRes
        )

        hourHandColor = typedArray.getColor(R.styleable.ClockView_hourHandColor, HOUR_HAND_COLOR)
        minuteHandColor =
            typedArray.getColor(R.styleable.ClockView_minuteHandColor, MINUTE_HAND_COLOR)
        secondHandColor =
            typedArray.getColor(R.styleable.ClockView_secondHandColor, SECOND_HAND_COLOR)

        hourHandLength = typedArray.getDimension(
            R.styleable.ClockView_hourHandLength, getDefaultHourHandLength(context)
        )
        minuteHandLength = typedArray.getDimension(
            R.styleable.ClockView_minuteHandLength, getDefaultMinuteHandLength(context)
        )
        secondHandLength = typedArray.getDimension(
            R.styleable.ClockView_secondHandLength, getDefaultSecondHandLength(context)
        )

        typedArray.recycle()
    }

    private fun initDefault() {
        hourHandColor = HOUR_HAND_COLOR
        minuteHandColor = MINUTE_HAND_COLOR
        secondHandColor = SECOND_HAND_COLOR

        hourHandLength = getDefaultHourHandLength(context)
        minuteHandLength = getDefaultMinuteHandLength(context)
        secondHandLength = getDefaultSecondHandLength(context)
    }

    private fun initPaints() {
        clockPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        clockPaint.color = Color.BLACK
        clockPaint.style = Paint.Style.STROKE
        clockPaint.strokeWidth = resources.getDimension(R.dimen.clockFacePaintStrokeWidth)

        hourHandPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        hourHandPaint.color = hourHandColor
        hourHandPaint.style = Paint.Style.STROKE
        hourHandPaint.strokeWidth = resources.getDimension(R.dimen.hourHandPaintStrokeWidth)

        minuteHandPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        minuteHandPaint.color = minuteHandColor
        minuteHandPaint.style = Paint.Style.STROKE
        minuteHandPaint.strokeWidth = resources.getDimension(R.dimen.minuteHandPaintStrokeWidth)

        secondHandPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        secondHandPaint.color = secondHandColor
        secondHandPaint.style = Paint.Style.STROKE
        secondHandPaint.strokeWidth = resources.getDimension(R.dimen.secondHandPaintStrokeWidth)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        val halfWeight = w / 2f
        val halfHeight = h / 2f

        centerX = halfWeight + paddingLeft - paddingRight
        centerY = halfHeight + paddingTop - paddingBottom
        radius = halfWeight - paddingLeft - paddingRight
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        drawClockFace(canvas)

        drawHourArrow(canvas, timeModel.hours)
        drawMinuteArrow(canvas, timeModel.minutes)
        drawSecondArrow(canvas, timeModel.seconds)
    }

    private fun drawClockFace(canvas: Canvas) {
        canvas.drawCircle(centerX, centerY, radius, clockPaint)

        val hoursRange = (0 until 12)

        for (i in hoursRange) {
            canvas.drawLine(
                centerX, centerY - radius,
                centerX, centerY - radius + SEGMENT_HIGH,
                clockPaint
            )
            canvas.rotate(ONE_HOUR_SEGMENT_DEGREE, centerX, centerY)
        }
    }

    private fun drawHourArrow(canvas: Canvas, hourValue: Float) {
        canvas.save()
        canvas.rotate(
            ONE_MINUTE_HOUR_DEGREE * timeModel.minutes + ONE_HOUR_SEGMENT_DEGREE * hourValue,
            centerX, centerY
        )
        canvas.drawLine(centerX, centerY, centerX, centerY - hourHandLength, hourHandPaint)
        canvas.restore()
    }

    private fun drawMinuteArrow(canvas: Canvas, minuteValue: Float) {
        canvas.save()
        canvas.rotate(
            ONE_SECOND_MINUTE_DEGREE * timeModel.seconds + ONE_SEGMENT_DEGREE * minuteValue,
            centerX, centerY
        )
        canvas.drawLine(centerX, centerY, centerX, centerY - minuteHandLength, minuteHandPaint)
        canvas.restore()
    }

    private fun drawSecondArrow(canvas: Canvas, secondValue: Float) {
        canvas.save()
        canvas.rotate(ONE_SEGMENT_DEGREE * secondValue, centerX, centerY)
        canvas.drawLine(centerX, centerY, centerX, centerY - secondHandLength, secondHandPaint)
        canvas.restore()

        canvas.drawCircle(centerX, centerY, RADIUS_SECOND_HAND_HEAD, secondHandPaint)
    }

    companion object {
        private const val SEGMENT_HIGH = 60f

        private const val RADIUS_SECOND_HAND_HEAD = 10f

        private const val HOUR_HAND_COLOR = Color.BLUE
        private const val MINUTE_HAND_COLOR = Color.BLACK
        private const val SECOND_HAND_COLOR = Color.RED

        private const val ONE_SEGMENT_DEGREE = 6f // 360/60 = 6
        private const val ONE_HOUR_SEGMENT_DEGREE = 30f // 360/12 = 30

        private const val ONE_SECOND_MINUTE_DEGREE = 0.1f // 360/60/60 = 0.1
        private const val ONE_MINUTE_HOUR_DEGREE = 0.5f // 360/12/60 = 0.5

        private fun getDefaultHourHandLength(context: Context): Float {
            return context.resources.getDimension(R.dimen.hourHandLength)
        }

        private fun getDefaultMinuteHandLength(context: Context): Float {
            return context.resources.getDimension(R.dimen.minuteHandLength)
        }

        private fun getDefaultSecondHandLength(context: Context): Float {
            return context.resources.getDimension(R.dimen.secondHandLength)
        }
    }
}