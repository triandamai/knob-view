package app.trian.knobview.snap

import app.trian.knobview.KnobView

class NoSnapEngine :SnapEngine() {
    override fun snapOnGoing(currentAngle: Float, knobView: KnobView) {

    }

    override fun snapEnd(currentAngle: Float, knobView: KnobView) {

    }
}