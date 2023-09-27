package app.trian.knobview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.view.MotionEventCompat
import app.trian.knobview.animation.KnobAnimator
import app.trian.knobview.animation.MinMaxKnobAnimator
import app.trian.knobview.command.KnobCommand
import app.trian.knobview.command.ResetCommand
import app.trian.knobview.command.ToggleEnabledCommand
import app.trian.knobview.snap.NoSnapEngine
import app.trian.knobview.snap.SnapEngine
import app.trian.knobview.source.KnobDrawableSource
import app.trian.knobview.source.KnobImageSource
import app.trian.knobview.source.KnobState
import app.trian.knobview.source.STATE_KEY
import app.trian.knobview.source.SUPER_KEY
import kotlin.math.abs
import kotlin.math.atan2


class KnobView : View, GestureDetector.OnGestureListener {
    private val DOUBLE_TAP_INTERVAL_MS = 200

    private lateinit var gestureDetector: GestureDetector
    private var bitmapPaint: Paint = Paint()
    private lateinit var knobDrawable: Drawable

    var config: KnobConfiguration = KnobDefaults.config(
        min = 60f,
        max = 300f,
        start = 60f
    )

    var currentAngle: Float = 0f
    private var previousAngle: Float = 0f
    private var previousX: Float? = null
    private var previousY: Float? = null
    private var lastTouchUp: Long = 0

    private var knobAnimator: KnobAnimator? = null
    private var knobImageSource: KnobImageSource? = null
    private var snapEngine: SnapEngine? = null

    private var doubleTapCommand: KnobCommand? = null
    private var longPressCommand: KnobCommand? = null

    private lateinit var listener: KnobListener
    var isKnobEnabled: Boolean = true

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        readAttrs(attrs, context)
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        readAttrs(attrs, context)
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ) {
        readAttrs(attrs, context)
        init(context)
    }

    private fun readAttrs(attrs: AttributeSet, context: Context) {
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.KnobView, 0, 0)
        try {
            val startingAngle =
                a.getInteger(R.styleable.KnobView_startingAngle, config.max.toInt()).toFloat()
            config = config.copy(startingAngle = startingAngle)
            knobDrawable = a.getDrawable(R.styleable.KnobView_knobSrc)!!
        } finally {
            a.recycle()
        }
    }

    private fun init(context: Context) {
        this.listener = object : KnobListener {
            override fun onValueChanged(value: Float) {}
        }
        bitmapPaint.isAntiAlias = true
        bitmapPaint.isFilterBitmap = true
        setWillNotDraw(false)

        knobImageSource = KnobDrawableSource(knobDrawable)
        knobAnimator = MinMaxKnobAnimator()
        snapEngine = NoSnapEngine()

        doubleTapCommand = ResetCommand(this)
        longPressCommand = ToggleEnabledCommand(this)
        gestureDetector = GestureDetector(context, this)

        currentAngle = config.startingAngle
    }

    fun setRotorAngle(targetAngle: Float) {
        var targetAngleState = targetAngle

        when (config.type) {
            KnobType.INDETERMINATE -> {
                currentAngle = targetAngleState
            }

            KnobType.WITH_MAX_MIN -> {
                if (targetAngle % 360 > config.max) {
                    targetAngleState = config.max
                    if (isAnimating()) {
                        knobAnimator?.stopAnimation();
                    }
                }

                if (targetAngle % 360 < config.min) {
                    targetAngleState = config.min;
                    if (isAnimating()) {
                        knobAnimator?.stopAnimation();
                    }
                }
                currentAngle = targetAngleState
            }
        }
        invalidate()
        notifyListener()
    }

    fun animateTo(angle: Float) {
        knobAnimator?.stopAnimation()
        knobAnimator?.animatedTo(this, angle)
    }

    fun pointAngle(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        var x1State = x1 - 0.5f
        var y1State = y1 - 0.5f
        var x2State = x2 - 0.5f
        var y2State = y2 - 0.5f

        var angle = Math.toDegrees(
            atan2(y2State.toDouble(), x2State.toDouble()) - atan2(
                y1State.toDouble(),
                x1State.toDouble()
            )
        ).toFloat()

        if (angle < -180) {
            angle += 360
        }

        if (angle > 180) {
            angle -= 360
        }

        return angle
    }

    fun getNormalizeValue(): Float {
        return currentAngle - config.min / config.max - config.min
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (knobImageSource?.hasBitmap() != true) {
            knobImageSource?.createBitmap(
                context,
                width,
                height
            )
        }

        if (canvas != null) {
            canvas.save()
            canvas.rotate(currentAngle, (width / 2).toFloat(), (height / 2).toFloat())
            if (knobImageSource != null) {
                canvas.drawBitmap(
                    knobImageSource?.bitmap!!, 0f, 0f, bitmapPaint
                )
            }
            canvas.restore()
        }
    }

    override fun onShowPress(e: MotionEvent) = Unit

    override fun onSingleTapUp(e: MotionEvent): Boolean = false

    override fun onFling(
        e1: MotionEvent,
        e2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean = false

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (isAnimating()) {
            return true
        }

        val action = MotionEventCompat.getActionMasked(event)
        when (action) {
            MotionEvent.ACTION_DOWN -> Unit
            MotionEvent.ACTION_MOVE -> Unit
            MotionEvent.ACTION_UP -> {
                val currentTimeMs = System.currentTimeMillis()
                if (currentTimeMs - lastTouchUp <= DOUBLE_TAP_INTERVAL_MS) {
                    onDoubleTap()
                } else {
                    if (abs(currentAngle - previousAngle) > 0) {
                        snapEngine?.snapEnd(currentAngle, this)
                    }
                }
                lastTouchUp = currentTimeMs
                notifyListener()
            }
        }

        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event)

    }

    override fun onDown(e: MotionEvent): Boolean {
        if (!isKnobEnabled) {
            return false
        }

        parent.requestDisallowInterceptTouchEvent(true)
        val x = (x / width.toFloat())
        val y = (y / height.toFloat())

        previousX = x
        previousY = y
        return true
    }

    override fun onScroll(
        e1: MotionEvent,
        e2: MotionEvent,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        if (!isKnobEnabled) {
            return false
        }

        parent.requestDisallowInterceptTouchEvent(true)
        val x = e2.x / width
        val y = e2.y / height

        val angleExp = pointAngle(previousX ?: 0f, previousY ?: 0f, x, y)

        previousX = x
        previousY = y

        previousAngle = currentAngle
        currentAngle += angleExp

        snapEngine?.snapOnGoing(currentAngle, this)

        setRotorAngle(currentAngle)

        return true
    }

    override fun onLongPress(e: MotionEvent) {
        if (longPressCommand != null) {
            longPressCommand?.execute(isKnobEnabled)
        }
    }

    fun onDoubleTap() {
        if (doubleTapCommand != null) {
            doubleTapCommand?.execute(isKnobEnabled)
        }
    }

    private fun notifyListener() {
        listener.onValueChanged(getNormalizeValue())
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        var size = if (widthMode == MeasureSpec.EXACTLY && widthSize > 0) {
            widthSize
        } else if (heightMode == MeasureSpec.EXACTLY && heightSize > 0) {
            heightSize
        } else {
            if (widthSize < heightSize) widthSize else heightSize
        }

        val finalMeasureSpec = MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY)

        super.onMeasure(finalMeasureSpec, finalMeasureSpec)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        knobImageSource?.createBitmap(context, w, h)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (isAnimating()) {
            knobAnimator?.stopAnimation()
        }
    }

    override fun onSaveInstanceState(): Parcelable? {
        val bundle = Bundle()
        bundle.putParcelable(SUPER_KEY, super.onSaveInstanceState())

        val knobState = KnobState();
        knobState.save(this)
        bundle.putParcelable(STATE_KEY, knobState)
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is Bundle) {
            val bundle = state as Bundle

            val newState = bundle.getParcelable<Parcelable>(SUPER_KEY)
            val knobState = bundle.getParcelable<KnobState>(STATE_KEY)

            knobState?.restore(this)
        }
        super.onRestoreInstanceState(state)
    }

    fun setListener(listener: KnobListener) {
        this.listener = listener
    }

    private fun isAnimating(): Boolean {
        return knobAnimator != null && knobAnimator?.isAnimating() == true
    }

}