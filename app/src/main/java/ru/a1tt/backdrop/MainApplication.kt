package ru.a1tt.backdrop

import android.app.Application
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable

class MainApplication: Application() {

    companion object {
        var appsIcons: HashMap<String, Bitmap> = HashMap()

        fun getBitmapFromVectorDrawable(drawable: Drawable): Bitmap {
            val bitmap = Bitmap.createBitmap(120,
                120, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight())
            drawable.draw(canvas)

            return bitmap
        }
    }
}