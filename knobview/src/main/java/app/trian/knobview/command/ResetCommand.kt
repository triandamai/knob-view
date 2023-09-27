package app.trian.knobview.command

import app.trian.knobview.KnobView

class ResetCommand(
    private val knobView: KnobView
) :KnobCommand {

    override fun execute(isEnabled: Boolean) {
        if(isEnabled){
            knobView.animateTo(knobView.config.startingAngle)
        }
    }
}