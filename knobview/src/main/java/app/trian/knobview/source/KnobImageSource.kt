package app.trian.knobview.source

import android.content.Context
import android.graphics.Bitmap

abstract class KnobImageSource() {

    var bitmap:Bitmap? = null
    abstract fun createBitmap(context: Context,width:Int,height:Int):Bitmap

    fun hasBitmap():Boolean{
        return bitmap != null
    }

}