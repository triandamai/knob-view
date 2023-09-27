package app.trian.knobview.source

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable


class KnobDrawableSource(private var drawable: Drawable) : KnobImageSource() {

    override fun createBitmap(context: Context, width: Int, height: Int): Bitmap {
        if(bitmap != null){
            if(bitmap?.isRecycled == false) {
                bitmap?.recycle()
            }
        }

        val unscaled = drawableToBitmap(drawable)

        bitmap = Bitmap.createScaledBitmap(unscaled,width,height,true)

        return bitmap!!
    }

    private fun drawableToBitmap(drawable: Drawable): Bitmap {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }
}