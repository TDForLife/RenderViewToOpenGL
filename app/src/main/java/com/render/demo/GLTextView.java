package com.render.demo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.SurfaceTexture;
import android.util.Log;
import android.view.Surface;
import android.view.Surface.OutOfResourcesException;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.TextView;

public class GLTextView extends TextView implements IRenderView {

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

    public GLTextView(Context context) {
        super(context);
        setLayoutParams(new LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, 400));
    }

    @Override
    public void onDraw(Canvas canvas) {

        if (mSurface != null) {
            try {
                final Canvas surfaceCanvas = mSurface.lockCanvas(null);
                surfaceCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
                super.onDraw(surfaceCanvas);
                mSurface.unlockCanvasAndPost(surfaceCanvas);
            } catch (OutOfResourcesException excp) {
                excp.printStackTrace();
            }
        }

        // Uncomment this if you want to show the original view
        // canvas.drawColor(Color.YELLOW);
        super.onDraw(canvas);
    }

}
