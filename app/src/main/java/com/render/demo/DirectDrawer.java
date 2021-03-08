package com.render.demo;

import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.util.Log;
import android.view.View;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class DirectDrawer {

    private static final String TAG = "draw";

    private final String VERTEX_SHADER_CODE = ""
            + "attribute vec4 vPosition;\n"
            + "attribute vec2 inputTextureCoordinate;\n"
            + "varying vec2 textureCoordinate;\n"
            + "uniform mat4 uMVPMatrix;\n"
            + "void main()" + "{\n"
//            + "     gl_Position = uMVPMatrix * vPosition;\n"
            + "     gl_Position = vPosition;\n"
            + "     textureCoordinate = inputTextureCoordinate;\n"
            + "}";

    private final String FRAGMENT_SHADER_CODE = ""
            + "#extension GL_OES_EGL_image_external : require\n"
            + "precision mediump float;\n"
            + "varying vec2 textureCoordinate;\n"
            + "uniform samplerExternalOES s_texture;\n"
            + "void main() {\n"
            + "     gl_FragColor = texture2D(s_texture, textureCoordinate);\n"
            + "}";

    // number of coordinates per vertex in this array
    private final static int COORDS_PER_VERTEX = 2;
    private final static int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per

    private final static float[] WORLD_COORDS = {
            -1.0f, 1.0f,
            -1.0f, -1.0f,
            1.0f, -1f,
            1.0f, 1.0f

//            -0.5f, 0.5f,
//            -0.5f, -0.5f,
//            0.5f, -0.5f,
//            0.5f, 0.5f

//            -0.30555555f,0.026352288f,
//            -0.30555555f,-0.026352288f,
//            0.30555555f,-0.026352288f,
//            0.30555555f,0.026352288f
    };

    private final static float[] TEXTURE_VERTICES = {
            0f, 0f,
            0f, 1f,
            1f, 1f,
            1f, 0f
    };

    // order to draw vertices
    private final short[] DRAW_ORDER = {
            0, 1, 2,
            0, 3, 2
    };


    private FloatBuffer mVertexBuffer;
    private FloatBuffer mTextureVerticesBuffer;
    private ShortBuffer mDrawListBuffer;
    private int mProgram;

    private int mPositionHandle;
    private int mPositionUniformHandle;
    private int mTextureCoordsHandle;
    private int mTextureID;

    private final float[] mMVPMatrix = new float[16];

    public DirectDrawer(int texture, int textureWidth, int textureHeight) {

        this.mTextureID = texture;

        // initialize vertex byte buffer for shape coordinates
        mVertexBuffer = ByteBuffer.allocateDirect(WORLD_COORDS.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        mVertexBuffer.put(WORLD_COORDS);
        mVertexBuffer.position(0);

        // initialize byte buffer for the draw list
        mDrawListBuffer = ByteBuffer.allocateDirect(DRAW_ORDER.length * 2)
                .order(ByteOrder.nativeOrder())
                .asShortBuffer();
        mDrawListBuffer.put(DRAW_ORDER);
        mDrawListBuffer.position(0);

        mTextureVerticesBuffer = ByteBuffer.allocateDirect(TEXTURE_VERTICES.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        mTextureVerticesBuffer.put(TEXTURE_VERTICES);
        mTextureVerticesBuffer.position(0);

        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, VERTEX_SHADER_CODE);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, FRAGMENT_SHADER_CODE);
        // create empty OpenGL ES Program
        mProgram = GLES20.glCreateProgram();
        // add the vertex shader
        GLES20.glAttachShader(mProgram, vertexShader);
        // add the fragment shader
        GLES20.glAttachShader(mProgram, fragmentShader);
        // shader to program
        GLES20.glLinkProgram(mProgram);

        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        mPositionUniformHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        mTextureCoordsHandle = GLES20.glGetAttribLocation(mProgram, "inputTextureCoordinate");

        // Matrix.perspectiveM(mMVPMatrix, 0, 45, (float) textureWidth / textureHeight, 0.1f, 100f);
        // Matrix.translateM(mMVPMatrix, 0, 0.5f, 0.5f, -0.5f);
        // Matrix.translateM(mMVPMatrix, 0, -0f, 0f, -0.1f);
        // printFloatArray(mMVPMatrix);
    }


    /**
     * mtx = {
     *  1.0,0.0,0.0,0.0,
     *  0.0,1.0,0.0,0.0,
     *  0.0,0.0,1.0,0.0,
     *  0.0,0.0,0.0,1.0
     * }
     * @param mtx
     */
    public void draw(float[] mtx) {

        GLES20.glUseProgram(mProgram);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, mTextureID);

        // get handle to vertex shader's vPosition member
        // Prepare the <insert shape here> coordinate data
        mVertexBuffer.position(0);
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, mVertexBuffer);

        mTextureVerticesBuffer.position(0);
        GLES20.glEnableVertexAttribArray(mTextureCoordsHandle);
        GLES20.glVertexAttribPointer(mTextureCoordsHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, mTextureVerticesBuffer);

        // Drawing
        // GLES20.glUniformMatrix4fv(mUniformPositionHandle, 1, false, mMVPMatrix, 0);
        GLES20.glDrawElements(GLES20.GL_TRIANGLE_STRIP, DRAW_ORDER.length, GLES20.GL_UNSIGNED_SHORT, mDrawListBuffer);
        // GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mTextureCoordsHandle);
    }

    private int loadShader(int type, String shaderCode) {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }

    public void transformWorldCoords(View targetView, int viewPortWidth, int viewPortHeight) {

        // 目标 View 在 ViewPort 的位置
        int targetLeft = targetView.getLeft();
        int targetTop = targetView.getTop() + targetView.getHeight();

        float ratioLeft = targetLeft * 1.0f / viewPortWidth;
        float ratioTop = targetTop * 1.0f / viewPortHeight;

        // 根据拉伸比例还原顶点
        float[] cube = new float[]{
                WORLD_COORDS[0] * ratioLeft, WORLD_COORDS[1] * ratioTop,
                WORLD_COORDS[2] * ratioLeft, WORLD_COORDS[3] * ratioTop,
                WORLD_COORDS[4] * ratioLeft, WORLD_COORDS[5] * ratioTop,
                WORLD_COORDS[6] * ratioLeft, WORLD_COORDS[7] * ratioTop,
        };

        mVertexBuffer.clear();
        mVertexBuffer.put(cube).position(0);
    }

    private void printFloatArray(float[] array) {
        StringBuilder builder = new StringBuilder();
        for (float element : array) {
            builder.append(element).append(",");
        }
        builder.deleteCharAt(builder.toString().length() - 1);
        Log.d(TAG, "FloatArray - " + builder.toString());
    }
}