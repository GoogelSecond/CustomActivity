package com.example.customactivity

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat
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
    private var millSecondHandColor by Delegates.notNull<Int>()

    private var hourHandLength by Delegates.notNull<Float>()
    private var minuteHandLength by Delegates.notNull<Float>()
    private var secondHandLength by Delegates.notNull<Float>()
    private var millSecondHandLength by Delegates.notNull<Float>()

    private var hourHandWith by Delegates.notNull<Float>()
    private var minuteHandWith by Delegates.notNull<Float>()
    private var secondHandWith by Delegates.notNull<Float>()
    private var millSecondHandWith by Delegates.notNull<Float>()

    private lateinit var clockFacePaint: Paint
    private lateinit var hourHandPaint: Paint
    private lateinit var minuteHandPaint: Paint
    private lateinit var secondHandPaint: Paint
    private lateinit var millSecondHandPaint: Paint

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
            timeModel = TimeModel(6f, 55f, 1f, 250f)
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
        hourHandLength = typedArray.getFloat(
            R.styleable.ClockView_hourHandLength,
            getDefaultHourHandLength()
        )
        hourHandWith = typedArray.getDimension(
            R.styleable.ClockView_hourHandWith,
            getDefaultHourHandWith()
        )

        minuteHandColor = typedArray.getColor(
            R.styleable.ClockView_minuteHandColor,
            MINUTE_HAND_COLOR
        )
        minuteHandLength =
            typedArray.getFloat(
                R.styleable.ClockView_minuteHandLength,
                getDefaultMinuteHandLength()
            )
        minuteHandWith = typedArray.getDimension(
            R.styleable.ClockView_minuteHandWith,
            getDefaultMinuteHandWith()
        )

        secondHandColor = typedArray.getColor(
            R.styleable.ClockView_secondHandColor,
            SECOND_HAND_COLOR
        )
        secondHandLength = typedArray.getFloat(
            R.styleable.ClockView_secondHandLength,
            getDefaultSecondHandLength()
        )
        secondHandWith = typedArray.getDimension(
            R.styleable.ClockView_secondHandWith,
            getDefaultSecondHandWith()
        )

        millSecondHandColor = typedArray.getColor(
            R.styleable.ClockView_millSecondHandColor,
            MILL_SECOND_HAND_COLOR
        )
        millSecondHandLength = typedArray.getFloat(
            R.styleable.ClockView_millSecondHandLength,
            getDefaultMIllSecondHandLength()
        )
        millSecondHandWith = typedArray.getDimension(
            R.styleable.ClockView_millSecondHandWith,
            getDefaultMIllSecondHandWith()
        )

        typedArray.recycle()
    }

    private fun initDefault() {
        hourHandColor = HOUR_HAND_COLOR
        minuteHandColor = MINUTE_HAND_COLOR
        secondHandColor = SECOND_HAND_COLOR
        millSecondHandColor = MILL_SECOND_HAND_COLOR

        hourHandLength = getDefaultHourHandLength()
        minuteHandLength = getDefaultMinuteHandLength()
        secondHandLength = getDefaultSecondHandLength()
        millSecondHandLength = getDefaultMIllSecondHandLength()

        hourHandWith = getDefaultHourHandWith()
        minuteHandWith = getDefaultMinuteHandWith()
        secondHandWith = getDefaultSecondHandWith()
        millSecondHandWith = getDefaultMIllSecondHandWith()
    }

    private fun getDefaultHourHandWith(): Float {
        return resources.getDimension(R.dimen.hourHandPaintStrokeWidth)
    }

    private fun getDefaultMinuteHandWith(): Float {
        return resources.getDimension(R.dimen.minuteHandPaintStrokeWidth)
    }

    private fun getDefaultSecondHandWith(): Float {
        return resources.getDimension(R.dimen.secondHandPaintStrokeWidth)
    }

    private fun getDefaultMIllSecondHandWith(): Float {
        return resources.getDimension(R.dimen.millSecondHandPaintStrokeWidth)
    }

    private fun getDefaultHourHandLength(): Float {
        return ResourcesCompat.getFloat(context.resources, R.dimen.hourHandLength)
    }

    private fun getDefaultMinuteHandLength(): Float {
        return ResourcesCompat.getFloat(context.resources, R.dimen.minuteHandLength)
    }

    private fun getDefaultSecondHandLength(): Float {
        return ResourcesCompat.getFloat(context.resources, R.dimen.secondHandLength)
    }

    private fun getDefaultMIllSecondHandLength(): Float {
        return ResourcesCompat.getFloat(context.resources, R.dimen.millSecondHandLength)
    }

    private fun initPaints() {
        clockFacePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        clockFacePaint.color = Color.BLACK
        clockFacePaint.style = Paint.Style.STROKE
        clockFacePaint.strokeWidth = resources.getDimension(R.dimen.clockFacePaintStrokeWidth)

        hourHandPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        hourHandPaint.color = hourHandColor
        hourHandPaint.style = Paint.Style.STROKE
        hourHandPaint.strokeWidth = hourHandWith

        minuteHandPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        minuteHandPaint.color = minuteHandColor
        minuteHandPaint.style = Paint.Style.STROKE
        minuteHandPaint.strokeWidth = minuteHandWith

        secondHandPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        secondHandPaint.color = secondHandColor
        secondHandPaint.style = Paint.Style.STROKE
        secondHandPaint.strokeWidth = secondHandWith

        millSecondHandPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        millSecondHandPaint.color = millSecondHandColor
        millSecondHandPaint.style = Paint.Style.STROKE
        millSecondHandPaint.strokeWidth = millSecondHandWith
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

        drawHourHand(canvas, timeModel.hours, timeModel.minutes)
        drawMinuteHand(canvas, timeModel.minutes, timeModel.seconds)
        drawSecondHand(canvas, timeModel.seconds, timeModel.millSeconds)
        drawMillSecondHand(canvas, timeModel.millSeconds)
    }

    private fun drawClockFace(canvas: Canvas) {
        canvas.drawCircle(centerX, centerY, radius, clockFacePaint)

        val hoursRange = (0 until 12)

        for (i in hoursRange) {
            canvas.drawLine(
                centerX, centerY - radius,
                centerX, centerY - radius * SEGMENT_HIGH,
                clockFacePaint
            )
            canvas.rotate(ONE_HOUR_SEGMENT_DEGREE, centerX, centerY)
        }
    }

    private fun drawHourHand(canvas: Canvas, hourValue: Float, minuteValue: Float) {
        canvas.save()
        canvas.rotate(
            ONE_MINUTE_HOUR_DEGREE * minuteValue + ONE_HOUR_SEGMENT_DEGREE * hourValue,
            centerX, centerY
        )
        canvas.drawLine(
            centerX, centerY + radius * HOUR_HAND_TAIL_LENGTH,
            centerX, centerY - radius * hourHandLength,
            hourHandPaint
        )
        canvas.restore()
    }

    private fun drawMinuteHand(canvas: Canvas, minuteValue: Float, secondValue: Float) {
        canvas.save()
        canvas.rotate(
            ONE_SECOND_MINUTE_DEGREE * secondValue + ONE_SEGMENT_DEGREE * minuteValue,
            centerX, centerY
        )
        canvas.drawLine(
            centerX, centerY + radius * MINUTE_HAND_TAIL_LENGTH,
            centerX, centerY - radius * minuteHandLength,
            minuteHandPaint
        )
        canvas.restore()
    }

    private fun drawSecondHand(canvas: Canvas, secondValue: Float, millSecondValue: Float) {
        canvas.save()
        canvas.rotate(
            ONE_MILL_SECOND_SECOND_DEGREE * millSecondValue + ONE_SEGMENT_DEGREE * secondValue,
            centerX, centerY
        )
        canvas.drawLine(
            centerX, centerY + radius * SECOND_HAND_TAIL_LENGTH,
            centerX, centerY - radius * secondHandLength,
            secondHandPaint
        )
        canvas.restore()
    }

    private fun drawMillSecondHand(canvas: Canvas, millSecondValue: Float) {
        canvas.save()
        canvas.rotate(
            ONE_MILL_SECOND_SEGMENT_DEGREE * millSecondValue,
            centerX, centerY
        )
        canvas.drawLine(
            centerX, centerY + radius * MILL_SECOND_HAND_TAIL_LENGTH,
            centerX, centerY - radius * millSecondHandLength,
            millSecondHandPaint
        )
        canvas.restore()

        millSecondHandPaint.style = Paint.Style.FILL
        canvas.drawCircle(centerX, centerY, RADIUS_MILL_SECOND_HAND_HEAD, millSecondHandPaint)
        millSecondHandPaint.style = Paint.Style.STROKE
    }

    companion object {
        private const val SEGMENT_HIGH = 0.9f

        private const val HOUR_HAND_TAIL_LENGTH = 0.15f
        private const val MINUTE_HAND_TAIL_LENGTH = 0.15f
        private const val SECOND_HAND_TAIL_LENGTH = 0.2f
        private const val MILL_SECOND_HAND_TAIL_LENGTH = 0.2f

        private const val RADIUS_MILL_SECOND_HAND_HEAD = 20f

        private const val HOUR_HAND_COLOR = Color.BLUE
        private const val MINUTE_HAND_COLOR = Color.BLACK
        private const val SECOND_HAND_COLOR = Color.RED
        private const val MILL_SECOND_HAND_COLOR = Color.GREEN

        private const val ONE_MILL_SECOND_SEGMENT_DEGREE = 0.36f // 360/1000 = 0.36
        private const val ONE_SEGMENT_DEGREE = 6f // 360/60 = 6
        private const val ONE_HOUR_SEGMENT_DEGREE = 30f // 360/12 = 30

        private const val ONE_MILL_SECOND_SECOND_DEGREE = 0.006f // 360/60/1000 = 0.006
        private const val ONE_SECOND_MINUTE_DEGREE = 0.1f // 360/60/60 = 0.1
        private const val ONE_MINUTE_HOUR_DEGREE = 0.5f // 360/12/60 = 0.5
    }
}