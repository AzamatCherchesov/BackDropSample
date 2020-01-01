package ru.a1tt.backdrop

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.FrameLayout

class BackLayout : FrameLayout {
    private var mDrawingDelegate: DrawingDelegate? = null

    constructor(context: Context) : super(context) {
        this.setWillNotDraw(false)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        this.setWillNotDraw(false)
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        mDrawingDelegate!!.onDrawBack(canvas)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        mDrawingDelegate!!.onLayoutBack(left, top, right, bottom)
    }

    internal fun setDrawing(drawingDelegate: DrawingDelegate) {
        mDrawingDelegate = drawingDelegate
    }

}
