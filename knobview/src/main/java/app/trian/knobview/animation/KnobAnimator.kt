package app.trian.knobview.animation

import app.trian.knobview.KnobView

abstract class KnobAnimator {
    protected var animating = false

    abstract fun animatedTo(view: KnobView, targetAngle:Float)

    abstract fun stopAnimation()

    fun isAnimating(): Boolean {
        return animating
    }
}