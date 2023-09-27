package app.trian.knobview.snap

import app.trian.knobview.KnobView
import java.security.InvalidParameterException
import kotlin.math.roundToInt


open class DivisionSnapEngine :SnapEngine {

    private var division:Int = 18

    constructor(division:Int):super(){
       setDivision(division)
    }

    override fun snapOnGoing(currentAngle: Float, knobView: KnobView) {
        //do nothing
    }

    override fun snapEnd(currentAngle: Float, knobView: KnobView) {
        val currentDivisionStep = calculateDivisionStep(currentAngle)
        val targetAngle = 360f / division * currentDivisionStep

        knobView.animateTo(targetAngle)
    }

    fun setDivision(division: Int) {
        if (division == 0) {
            throw InvalidParameterException("Congratulations. You played yourself. / division by 0.")
        }
        this.division = division
    }

    protected fun calculateDivisionStep(currentAngle:Float):Int{
        return (currentAngle / 360f * division).roundToInt()
    }
}