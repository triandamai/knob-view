package app.trian.knobview

enum class KnobType {
    INDETERMINATE,
    WITH_MAX_MIN
}

data class KnobConfiguration(
    val min: Float = 0f,
    val max: Float = 350f,
    val startingAngle:Float = 0f,
    val type: KnobType
)

object KnobDefaults {
    fun indeterminate(
        start:Float=0f
    ): KnobConfiguration = KnobConfiguration(
        type = KnobType.INDETERMINATE,
        min=0f,
        max=100f,
        startingAngle = start
    )

    fun config(
        min: Float = 0f,
        max: Float = 350f,
        start:Float=0f
    ): KnobConfiguration = KnobConfiguration(
        min = min,
        max = max,
        startingAngle=start,
        type = KnobType.WITH_MAX_MIN
    )
}
