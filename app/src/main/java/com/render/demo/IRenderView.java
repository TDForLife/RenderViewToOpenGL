package com.render.demo;

import android.graphics.SurfaceTexture;
import android.view.Surface;

public interface IRenderView {

    void configSurface(Surface surface);

    void configSurfaceTexture(SurfaceTexture surfaceTexture);
}
