package com.example.task4all.helpers

import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable

class ImageHelper {
    companion object {

        /**
         * Generates a bitmap from the given drawable, with a white circle corner
         */
        fun getRoundedDrawable(drawable: Drawable): Bitmap {

            val bitmap = (drawable as BitmapDrawable).bitmap
            val imageRounded = Bitmap.createBitmap(bitmap.width, bitmap.height, bitmap.config)
            val canvas = Canvas(imageRounded)
            val paint = Paint()
            paint.isAntiAlias = true
            paint.shader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)

            //Some of the images are not squares, then we need to draw a circle using the smaller dimension (w|h)
            val smallerDimension = (Math.min(bitmap.width, bitmap.height) / 2).toFloat()

            canvas.drawCircle(
                smallerDimension,
                smallerDimension,
                smallerDimension,
                paint
            )
            return imageRounded
        }
    }
}