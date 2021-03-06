package com.render.demo;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.SurfaceTexture;
import android.util.Log;
import android.view.Surface;
import android.view.Surface.OutOfResourcesException;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class GLLinearLayout extends LinearLayout implements IRenderView {

    private Surface mSurface;

    private SurfaceTexture mSurfaceTexture;


    @Override
    public void configSurface(Surface surface) {
        this.mSurface = surface;
    }

    @Override
    public void configSurfaceTexture(SurfaceTexture surfaceTexture) {
        this.mSurfaceTexture = surfaceTexture;
        mSurface = new Surface(mSurfaceTexture);
    }

    public GLLinearLayout(Context context) {
        super(context);
        setLayoutParams(new LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, 400));
        addOnPreDrawListener();
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
        } catch (OutOfResourcesException e) {
            e.printStackTrace();
        }
    }

}
