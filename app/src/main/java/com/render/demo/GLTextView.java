package com.render.demo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.Surface.OutOfResourcesException;
import android.widget.TextView;

public class GLTextView extends TextView implements IRenderView {

    private Surface mSurface;

    public GLTextView(Context context) {
        super(context);
    }

    public GLTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GLTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void configSurface(Surface surface) {
        this.mSurface = surface;
    }

    @Override
    public void onDraw(Canvas canvas) {

        if (mSurface != null) {
            try {
                final Canvas surfaceCanvas = mSurface.lockCanvas(null);
                surfaceCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
                super.onDraw(surfaceCanvas);
                mSurface.unlockCanvasAndPost(surfaceCanvas);
            } catch (OutOfResourcesException exception) {
                exception.printStackTrace();
            }
        }

        // Uncomment this if you want to show the original view
        canvas.drawColor(Color.YELLOW);
        super.onDraw(canvas);
    }

}
