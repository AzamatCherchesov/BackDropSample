package ru.a1tt.backdrop

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.annotation.IdRes
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlin.math.max
import kotlin.math.min

class BackDropBehavior : BottomSheetBehavior<View>, DrawingDelegate {
    private val backRect: Rect = Rect()
    private val fontRect: Rect = Rect()
    private var toolbar: Toolbar? = null
    private var backLayout: BackLayout? = null
    private var frontLayout: FrontLayout? = null

    private var backDrop: BackDrop? = null

    private val cornerRadiusDrawable = GradientDrawable()
    private var cornerRadius: Float = 0f
    private var currentCornerRadius: Float = 0f
    private val cornerArray: FloatArray =
        floatArrayOf(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f)

    private var heightPixels: Int = 0

    private var dropState: DropState = DropState.Closed
    private var currentRadius = 1.0F

    private var menuIconResId = R.drawable.ic_menu
        set(@IdRes customizedMenuIconResId: Int) {
            field = customizedMenuIconResId
        }
    private var closeIconResId = R.drawable.ic_close
        set(@IdRes customizedCloseIconResId: Int) {
            field = customizedCloseIconResId
        }

    constructor() : super()
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    @SuppressLint("ObsoleteSdkInt")
    internal fun setViews(
        backDrop: BackDrop,
        toolbarValue: Toolbar,
        backLayoutValue: BackLayout,
        frontLayoutValue: FrontLayout,
        cornerValue: Float,
        heightPixels: Int,
        frontLayoutBackgroundColor: Int
    ) {
        this.backDrop = backDrop
        this.toolbar = toolbarValue
        this.backLayout = backLayoutValue
        this.frontLayout = frontLayoutValue
        this.heightPixels = heightPixels

        if (android.os.Build.VERSION.SDK_INT >= 11) {
            backLayout!!.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        }

        frontLayout?.setDrawing(this)
        backLayout?.setDrawing(this)

        cornerRadius = cornerValue
        cornerRoundsDiff(0.0F)

        cornerRadiusDrawable.setColor(frontLayoutBackgroundColor)
        cornerRadiusDrawable.alpha = 0xF0

        isHideable = false
        state = STATE_COLLAPSED
        peekHeight = (heightPixels - toolbar!!.layoutParams.height)

        with(this.toolbar!!) {
            setNavigationOnClickListener {
                state = STATE_COLLAPSED
                dropState = when (dropState) {
                    DropState.Closed -> DropState.Opened
                    DropState.Opened -> DropState.Closed
                }
                drawDropState(this, backLayout!!)
            }
            setNavigationIcon(menuIconResId)
        }

        var lastSlideOffset = -1.0F
        setBottomSheetCallback(object : BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                Log.e("Corner---", "onSlide========== slideOffset$slideOffset")
                backLayout!!.invalidate()

                if (dropState == DropState.Closed) {
                    if (lastSlideOffset == -1.0F) {
                        lastSlideOffset = 1.0F - slideOffset
                    } else {
                        cornerRoundsDiff(1.0F - slideOffset - lastSlideOffset)
                        lastSlideOffset = 1.0F - slideOffset
                    }
                }

                if (frontLayout!!.y <= toolbar!!.bottom && dropState == DropState.Opened) {
                    close()
                }
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == STATE_EXPANDED) {
                    peekHeight = backDrop.getHeight() - toolbar!!.height
                }
            }
        })
    }

    override fun onDrawBack(canvas: Canvas) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.TRANSPARENT
            style = Paint.Style.FILL
            xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        }

        canvas.drawRect(
            backRect.left.toFloat(),
            frontLayout!!.top.toFloat(),
            backRect.right.toFloat(),
            backRect.bottom.toFloat(), paint
        )
    }

    override fun onLayoutBack(l: Int, t: Int, r: Int, b: Int) {
        backRect.set(l, t, r - l, b - t)
    }

    override fun onLayoutFront(l: Int, t: Int, r: Int, b: Int) {
        fontRect.set(l, t, r - l, b - t)
    }

    override fun onDrawFront(canvas: Canvas) {
        cornerRadiusDrawable.bounds = fontRect
        cornerRadiusDrawable.draw(canvas)
    }

    internal fun open(): Boolean {
        when (dropState) {
            is DropState.Opened -> {
                return false
            }
            is DropState.Closed -> {
                dropState = DropState.Opened
                if (backLayout != null && toolbar != null && frontLayout != null) {
                    drawDropState(toolbar!!, backLayout!!)
                } else {
                    throw IllegalArgumentException("FrontLayout, toolbar and backLayout must be initialized")
                }
                return true
            }
        }
    }

    internal fun close(): Boolean {
        when (dropState) {
            is DropState.Closed -> {
                return false
            }
            is DropState.Opened -> {
                dropState = DropState.Closed
                if (backLayout != null && toolbar != null && frontLayout != null) {
                    drawDropState(toolbar!!, backLayout!!)
                } else {
                    throw IllegalArgumentException("FrontLayout, toolbar and backLayout must be initialized")
                }
                return true
            }
        }
    }

    private fun drawDropState(toolbar: Toolbar, backContainer: View) {
        when (dropState) {
            DropState.Closed -> {
                toolbar.setNavigationIcon(menuIconResId)
                peekHeight = backDrop!!.getHeight() - toolbar.height
            }
            DropState.Opened -> {
                toolbar.setNavigationIcon(closeIconResId)
                peekHeight = backDrop!!.getHeight() - backContainer.height
            }
        }
    }

    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int
    ) {
        if (dy < 0 && dropState == DropState.Closed && (state == STATE_EXPANDED || state == 1)) {
            consumed[0] = dx
            consumed[1] = dy

            if ((frontLayout!!.top - dy) >= toolbar!!.height) {
                state = STATE_COLLAPSED
                return
            }

            val offset = min(-dy, toolbar!!.height)
            Log.e("A1tt-------------", "offset=$offset")
            frontLayout!!.offsetTopAndBottom(offset)
            frontLayout!!.invalidate()
            backLayout!!.invalidate()

            return
        }
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
    }

    override fun onNestedPreFling(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        target: View,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        if (velocityY < 0) {
            return false
        }
        return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY)
    }

    fun onActionUp() {
        if (state != STATE_COLLAPSED
            && frontLayout!!.y > 0
        ) {
            state = STATE_COLLAPSED
        }
    }

    fun cornerRoundsDiff(diff: Float) {
        Log.e("A1rr----", "diff=$diff")
        currentRadius = if (diff > 0) {
            if (backDrop!!.getPosition() < 2) {
                min(1.0F, currentRadius + diff)
            } else {
                currentRadius
            }
        } else {
            max(0.0F, currentRadius + diff)
        }
        Log.e("A1rr----", "currentRadius=$currentRadius")
        currentCornerRadius = cornerRadius * currentRadius
        val fArr = cornerArray
        fArr[3] = currentCornerRadius
        fArr[2] = currentCornerRadius
        fArr[1] = currentCornerRadius
        fArr[0] = currentCornerRadius
        cornerRadiusDrawable.cornerRadii = fArr
        frontLayout!!.invalidate()
    }

    sealed class DropState {
        object Closed : DropState()
        object Opened : DropState()
    }
}