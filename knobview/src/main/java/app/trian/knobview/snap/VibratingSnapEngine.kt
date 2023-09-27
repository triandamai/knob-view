package app.trian.knobview.snap

import android.os.Vibrator
import app.trian.knobview.KnobView

class VibratingSnapEngine(
    private val vibrator: Vibrator,
    private val division: Int,
    private val vibrationTimeMS: Int
) : DivisionSnapEngine(division) {
    private var previousDivisionStep = -1
    private var vibratingTimeMs:Int = vibrationTimeMS

    override fun snapEnd(currentAngle: Float, knobView: KnobView) {
        super.snapEnd(currentAngle, knobView)
        previousDivisionStep = -1
    }

    override fun snapOnGoing(currentAngle: Float, knobView: KnobView) {
        if(previousDivisionStep == -1){
            previousDivisionStep = calculateDivisionStep(currentAngle)
            return
        }

        val currentDivisionStep = calculateDivisionStep(currentAngle)
        if((currentDivisionStep - previousDivisionStep != 0)){
            previousDivisionStep = currentDivisionStep
            vibrator.vibrate(vibratingTimeMs.toLong())
        }
    }
}