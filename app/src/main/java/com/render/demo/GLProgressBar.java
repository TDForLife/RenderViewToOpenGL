package com.render.demo;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.SurfaceTexture;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.Surface.OutOfResourcesException;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

public class GLProgressBar extends ProgressBar implements IRenderView {

	private Surface mSurface;

	public GLProgressBar(Context context) {
		super(context);
		setLayoutParams(new LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, 400));
	}

	public GLProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public GLProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	public void configSurface(Surface surface) {
		this.mSurface = surface;
	}

	@Override
	public void configSurfaceTexture(SurfaceTexture surfaceTexture) {
		mSurface = new Surface(surfaceTexture);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (mSurface != null) {
			// Requires a try/catch for .lockCanvas( null )
			try {
				final Canvas surfaceCanvas = mSurface.lockCanvas(null); // Android
				surfaceCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
				// canvas
				// from
				// surface
				super.onDraw(surfaceCanvas); // Call the WebView onDraw
				// targetting the canvas
				mSurface.unlockCanvasAndPost(surfaceCanvas); // We're done with
				// the canvas!
			} catch (OutOfResourcesException excp) {
				excp.printStackTrace();
			}
		}

//		if (mSurface != null) {
//			mSurface.release();
//			mSurface = null;
//			mSurface = new Surface(mSurfaceTexture);
//		}
//		canvas.drawColor(0, PorterDuff.Mode.CLEAR);

		// original view
		// <- Uncomment this if you want to show the original view
		super.onDraw( canvas );
	}
}
