package com.render.demo;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.view.Display;
import android.view.Surface;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

class ViewRenderer implements GLSurfaceView.Renderer {

    private final int GL_TEXTURE_EXTERNAL_OES = 0x8D65;

    // Fixed values
    private int textureWidth;
    private int textureHeight;

    private Context context;
    private IRenderView renderView;
    private int glSurfaceTex;
    private SurfaceTexture surfaceTexture = null;
    private DirectDrawer directDrawer;

    public ViewRenderer(Context context, IRenderView renderView, Display mDisplay) {
        this.context = context;
        this.renderView = renderView;
        textureWidth = mDisplay.getWidth();
        textureHeight = mDisplay.getHeight();
//        textureWidth = 277;
//        textureHeight = 57;
    }

    @Override
    public void onDrawFrame(GL10 gl) {

        surfaceTexture.updateTexImage();

        GLES20.glClearColor(0.0f, 0.0f, 1.0f, 1.0f);
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        float[] mtx = new float[16];
        surfaceTexture.getTransformMatrix(mtx);
        directDrawer.draw(mtx);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        surfaceTexture = null;
        glSurfaceTex = engineCreateSurfaceTexture(textureWidth, textureHeight);
        if (glSurfaceTex > 0) {
            surfaceTexture = new SurfaceTexture(glSurfaceTex);
            surfaceTexture.setDefaultBufferSize(textureWidth, textureHeight);
            Surface surface = new Surface(surfaceTexture);
            renderView.configSurface(surface);
            renderView.configSurfaceTexture(surfaceTexture);
            directDrawer = new DirectDrawer(glSurfaceTex);
        }
    }


    /**
     * Create our texture. This has to be done each time the surface is
     * created.
     */
    int engineCreateSurfaceTexture(int width, int height) {

        int[] textures = new int[1];
        GLES20.glGenTextures(1, textures, 0);

        glSurfaceTex = textures[0];

        if (glSurfaceTex > 0) {
            GLES20.glBindTexture(GL_TEXTURE_EXTERNAL_OES, glSurfaceTex);

            // Notice the use of GL_TEXTURE_2D for texture creation
            GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGB, width, height, 0,
                    GLES20.GL_RGB, GLES20.GL_UNSIGNED_BYTE, null);

            GLES20.glTexParameteri(GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
            GLES20.glTexParameteri(GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
            GLES20.glTexParameteri(GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameteri(GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        }
        return glSurfaceTex;
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

    }
}