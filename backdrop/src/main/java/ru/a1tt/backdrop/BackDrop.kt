package ru.a1tt.backdrop

import android.content.res.Resources
import androidx.annotation.ColorInt
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.absoluteValue

class BackDrop(
    private val backDropCoordinator: CoordinatorLayout,
    private val frontLayout: FrontLayout,
    @ColorInt private val frontColor: Int,
    frontLayoutCorner: Float = 36.0F,
    backLayout: BackLayout,
    toolbar: Toolbar,
    resources: Resources,
    statusBarHeight: Int
) {
    private var recyclerView: RecyclerView? = null
    private val behavior: BackDropBehavior
    var lastCornerValue = -1.0F

    init {
        behavior = getBehavior()
        behavior.setViews(
            this,
            toolbar,
            backLayout,
            frontLayout,
            frontLayoutCorner,
            resources.displayMetrics.heightPixels - resources.getDimensionPixelSize(
                resources.getIdentifier(
                    "status_bar_height",
                    "dimen",
                    "android"
                )
            ),
            frontColor
        )
        frontLayout.setBackDropBehavior(behavior)
    }

    fun getPosition(): Int {
        return (recyclerView?.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
    }

    fun getHeight() = backDropCoordinator.bottom


    fun close() = behavior.close()


    private fun getBehavior(): BackDropBehavior {
        frontLayout.layoutParams.run {
            if (this !is CoordinatorLayout.LayoutParams) {
                throw IllegalArgumentException("View's layout params should be CoordinatorLayout.LayoutParams")
            }

            if (this.behavior !is BackDropBehavior) {
                throw IllegalArgumentException("Behavior should be BackDropBehavior")
            }

            return this.behavior as BackDropBehavior
        }
    }


    fun setRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView
        this.recyclerView!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = (recyclerView.layoutManager as LinearLayoutManager)
                if (dy < 0 && (layoutManager.findFirstVisibleItemPosition()) < 2) {
                    val firstView = layoutManager.findViewByPosition(0)
                    firstView?.let {
                        if (lastCornerValue == -1.0F) {
                            lastCornerValue =
                                (firstView.height - firstView.top.absoluteValue).toFloat() / firstView.height.toFloat()
                        } else {
                            val newCornerValue =
                                (firstView.height - firstView.top.absoluteValue).toFloat() / firstView.height.toFloat()
                            behavior.cornerRoundsDiff(newCornerValue - lastCornerValue)
                            lastCornerValue = newCornerValue
                        }

                    }
                }
                if (dy > 0 && lastCornerValue != -1.0F) lastCornerValue = -1.0F

                super.onScrolled(recyclerView, dx, dy)
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
    }
}