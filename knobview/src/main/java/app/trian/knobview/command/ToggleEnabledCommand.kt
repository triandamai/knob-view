package app.trian.knobview.command

import app.trian.knobview.KnobView

class ToggleEnabledCommand(
    private val knobView: KnobView
) :KnobCommand {
    override fun execute(isEnabled: Boolean) {
        knobView.isKnobEnabled = !knobView.isKnobEnabled
    }


}