package ru.a1tt.backdrop;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class FrontLayout extends FrameLayout {
    private DrawingDelegate mDrawingDelegate = null;
    private BackDropBehavior mBackDropBehavior;

    public FrontLayout(@NonNull Context context) {
        super(context);
        this.setWillNotDraw(false);
    }

    public FrontLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.setWillNotDraw(false);
    }

    public FrontLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setWillNotDraw(false);
    }

    public FrontLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.setWillNotDraw(false);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.e("A1tt====", "dispatchTouchEvent= " + ev);
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            mBackDropBehavior.onActionUp();
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mDrawingDelegate.onDrawFront(canvas);
    }

    @Override
    public void onDrawForeground(Canvas canvas) {
        super.onDrawForeground(canvas);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mDrawingDelegate.onLayoutFront(left, top, right, bottom);
    }

    public void setDrawing(DrawingDelegate drawingDelegate) {
        mDrawingDelegate = drawingDelegate;
    }

    void setBackDropBehavior(BackDropBehavior backDropBehavior) {
        mBackDropBehavior = backDropBehavior;
    }
}
