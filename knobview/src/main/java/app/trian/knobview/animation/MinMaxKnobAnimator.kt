package app.trian.knobview.animation

import android.os.Handler
import android.os.Looper
import app.trian.knobview.KnobView
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.roundToInt


class MinMaxKnobAnimator : KnobAnimator() {

    //~60fps
    private val UPDATE_INTERVAL_MS: Long = 16
    var maxAnimateTimeMS = 1024
    var minAnimateTimeMS = 128

    private var repeatCount = 0
    private var step = 0f
    private var knobView: KnobView? = null
    private val handler: Handler = Handler(Looper.myLooper()!!)
    private var targetAngle = 0f

    override fun animatedTo(view: KnobView, targetAngle: Float) {
        this.knobView = view
        this.targetAngle = targetAngle

        val currentAngle = view.currentAngle
        val route = (targetAngle - currentAngle) % 360

        var routeAnimateTime = ((abs(route) / 360f) * maxAnimateTimeMS).roundToInt()
        routeAnimateTime = max(minAnimateTimeMS, routeAnimateTime)

        repeatCount = routeAnimateTime / UPDATE_INTERVAL_MS.toInt()
        step = route / repeatCount

        handler.postDelayed({
            if (repeatCount > 0) {
                knobView?.setRotorAngle((view.currentAngle) + step)
                repeatCount--
            } else {
                knobView?.setRotorAngle(targetAngle)
                animating = false
                knobView = null
            }
        }, UPDATE_INTERVAL_MS)
        animating = true
    }

    override fun stopAnimation() {
        handler.removeCallbacksAndMessages(null)
        animating = false
    }


}