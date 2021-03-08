package com.render.demo;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.view.Surface;
import android.view.View;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

class ViewRenderer implements GLSurfaceView.Renderer {

    private final int GL_TEXTURE_EXTERNAL_OES = 0x8D65;

    private Context context;
    private IRenderView mRenderView;
    private int glOuterSurfaceTextureID;
    private SurfaceTexture surfaceTexture = null;
    private DirectDrawer mDirectDrawer;

    // Fixed values
    private int textureWidth;
    private int textureHeight;

    public ViewRenderer(Context context, IRenderView mRenderView) {
        this.context = context;
        this.mRenderView = mRenderView;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        textureWidth = width;
        textureHeight = height;
        // textureWidth = 277;
        // textureHeight = 57;

        if (surfaceTexture != null) {
            surfaceTexture.release();
            surfaceTexture = null;
        }
        glOuterSurfaceTextureID = engineCreateGLOuterSurfaceTexture(textureWidth, textureHeight);
        if (glOuterSurfaceTextureID > 0) {
            surfaceTexture = new SurfaceTexture(glOuterSurfaceTextureID);
            surfaceTexture.setDefaultBufferSize(textureWidth, textureHeight);
            Surface surface = new Surface(surfaceTexture);
            mRenderView.configSurface(surface);
            mDirectDrawer = new DirectDrawer(glOuterSurfaceTextureID, textureWidth, textureHeight);
        }
    }

    @Override
    public void onDrawFrame(GL10 gl) {

        if (surfaceTexture == null || mDirectDrawer == null) {
            return;
        }

        surfaceTexture.updateTexImage();

        // 你试着把 glClearColor 去掉，你会发现其实 SurfaceView 上早就有内容了，只不过被 glClearColor 给抹掉了
        GLES20.glClearColor(0.0f, 0.0f, 1.0f, 1.0f);
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        float[] mtx = new float[16];
        surfaceTexture.getTransformMatrix(mtx);
        mDirectDrawer.draw(mtx);
    }

    /**
     * Create our texture. This has to be done each time the surface is
     * created.
     */
    private int engineCreateGLOuterSurfaceTexture(int width, int height) {

        int[] textures = new int[1];
        GLES20.glGenTextures(1, textures, 0);

        glOuterSurfaceTextureID = textures[0];

        if (glOuterSurfaceTextureID > 0) {
            GLES20.glBindTexture(GL_TEXTURE_EXTERNAL_OES, glOuterSurfaceTextureID);

            // Notice the use of GL_TEXTURE_2D for texture creation
            GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGB, width, height, 0,
                    GLES20.GL_RGB, GLES20.GL_UNSIGNED_BYTE, null);

            GLES20.glTexParameteri(GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
            GLES20.glTexParameteri(GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
            GLES20.glTexParameteri(GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameteri(GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        }
        return glOuterSurfaceTextureID;
    }

    public void transformWorldCoords(View targetView) {
        mDirectDrawer.transformWorldCoords(targetView, textureWidth, textureHeight);
    }
}