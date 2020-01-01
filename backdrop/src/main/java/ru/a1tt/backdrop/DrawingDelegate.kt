package ru.a1tt.backdrop

import android.graphics.Canvas

internal interface DrawingDelegate {

    fun onDrawBack(canvas: Canvas)

    fun onDrawFront(canvas: Canvas)

    fun onLayoutFront(l: Int, t: Int, r: Int, b: Int)

    fun onLayoutBack(l: Int, t: Int, r: Int, b: Int)
}
