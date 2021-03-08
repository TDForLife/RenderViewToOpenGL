package com.render.demo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.SurfaceTexture;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.Surface.OutOfResourcesException;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

public class GLLinearLayout extends LinearLayout implements IRenderView {

    private Surface mSurface;

    public GLLinearLayout(Context context) {
        super(context);
        setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        addOnPreDrawListener();
    }

    public GLLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GLLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void configSurface(Surface surface) {
        this.mSurface = surface;
    }

    private void addOnPreDrawListener() {
        final ViewTreeObserver mObserver = getViewTreeObserver();
        if (mObserver != null) {
            mObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    // View 或者子 view 发生变化
                    if (isDirty()) {
                        invalidate();
                    }
                    return true;
                }
            });
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        try {
            if (mSurface != null) {
                Canvas surfaceCanvas = mSurface.lockCanvas(null);
                surfaceCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
                super.dispatchDraw(surfaceCanvas);
                mSurface.unlockCanvasAndPost(surfaceCanvas);
            }
            invalidate();
        } catch (OutOfResourcesException exception) {
            exception.printStackTrace();
        }
    }

}
