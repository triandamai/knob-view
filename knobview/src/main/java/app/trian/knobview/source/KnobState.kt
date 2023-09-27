package app.trian.knobview.source

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import app.trian.knobview.KnobDefaults
import app.trian.knobview.KnobView

val SUPER_KEY = "superStateKey"
val STATE_KEY = "stateKey"

class KnobState : Parcelable {




    private var minAngle = 0f
    private var maxAngle: Float = 0f
    private var startingAngle: Float = 0f
    private var currentAngle: Float = 0f

    constructor()
    constructor(input: Parcel) {
        minAngle = input.readFloat()
        maxAngle = input.readFloat()
        startingAngle = input.readFloat()
        currentAngle = input.readFloat()
    }

    fun save(knobView: KnobView) {
        minAngle = knobView.config.min
        maxAngle = knobView.config.max
        startingAngle = knobView.config.startingAngle
        currentAngle = knobView.currentAngle
    }

    fun restore(knobView: KnobView) {
        knobView.config = KnobDefaults.config(
            min = minAngle,
            max = maxAngle
        )
        knobView.config = knobView.config.copy(
            startingAngle=startingAngle
        )
        knobView.setRotorAngle(currentAngle)
    }


    override fun describeContents(): Int = 0


    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeFloat(minAngle)
        dest.writeFloat(maxAngle)
        dest.writeFloat(startingAngle)
        dest.writeFloat(currentAngle)
    }

    companion object CREATOR : Creator<KnobState?> {
        override fun createFromParcel(source: Parcel?): KnobState {
            return KnobState()
        }

        override fun newArray(size: Int): Array<KnobState?> {
            return arrayOfNulls(size)
        }

    }
}