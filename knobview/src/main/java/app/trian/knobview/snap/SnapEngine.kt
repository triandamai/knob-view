package app.trian.knobview.snap

import app.trian.knobview.KnobView

abstract class SnapEngine {
    abstract fun snapOnGoing(currentAngle: Float,knobView: KnobView)
    abstract fun snapEnd(currentAngle:Float,knobView: KnobView)
}