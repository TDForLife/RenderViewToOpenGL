package com.render.demo;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

public class GLActivity extends Activity {

    private static final String TAG = "activity";

    private FrameLayout root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        root = findViewById(R.id.container_layout);
//        addTextView();
//        addGLViewGroup();
        addProgressView();
    }


    private void addProgressView() {

        Display display = getWindowManager().getDefaultDisplay();
        final GLProgressBar glProgressBar = new GLProgressBar(this);

        ViewRenderer renderer = new ViewRenderer(getApplicationContext(), glProgressBar, display);
        GLSurfaceView glSurfaceView = new GLSurfaceView(getApplicationContext());
        glSurfaceView.setEGLContextClientVersion(2);
        glSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        glSurfaceView.setRenderer(renderer);

        final FrameLayout.LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        glSurfaceView.setLayoutParams(layoutParams);
        root.addView(glSurfaceView, layoutParams);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                root.addView(glProgressBar, layoutParams);
            }
        }, 100);
    }

    private void addTextView() {
        Display mDisplay = getWindowManager().getDefaultDisplay();
        final GLTextView glTextView = new GLTextView(this);
        glTextView.setText("Hello TextView");
        glTextView.setTextColor(Color.WHITE);

        ViewRenderer renderer = new ViewRenderer(getApplicationContext(), glTextView, mDisplay);

        final GLSurfaceView glSurfaceView = new GLSurfaceView(getApplicationContext());
        glSurfaceView.setEGLContextClientVersion(2);
        glSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        glSurfaceView.setRenderer(renderer);

        final FrameLayout.LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        root.addView(glSurfaceView, layoutParams);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                root.addView(glTextView, layoutParams);
            }
        }, 100);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startPopsAnimTrans(glTextView);
                // glTextView.setLeft(100);
                // glTextView.postInvalidate();
            }
        }, 2000);
    }

    private void addGLViewGroup() {
        Display mDisplay = getWindowManager().getDefaultDisplay();
        final GLTextView glTextView = new GLTextView(this);
        glTextView.setText("Hello GLViewGroup");
        glTextView.setTextColor(Color.WHITE);
        final GLLinearLayout glLinearLayout = new GLLinearLayout(this);
        glLinearLayout.setWillNotDraw(false);
        glLinearLayout.addView(glTextView);

        // Setup the surface view for drawing to
        ViewRenderer renderer = new ViewRenderer(getApplicationContext(), glLinearLayout, mDisplay);
        final GLSurfaceView glSurfaceView = new GLSurfaceView(getApplicationContext());
        glSurfaceView.setEGLContextClientVersion(2);
        glSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        glSurfaceView.setRenderer(renderer);

        final FrameLayout.LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        root.addView(glSurfaceView, layoutParams);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                root.addView(glLinearLayout, layoutParams);
            }
        }, 100);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startPopsAnimTrans(glLinearLayout);
                // glTextView.setLeft(100);
                // glLinearLayout.postInvalidate();
            }
        }, 2000);
    }


    // 属性动画-平移
    private void startPopsAnimTrans(final View view) {
        float[] x = {60f};
        ObjectAnimator objectAnimatorX = ObjectAnimator.ofFloat(view, "translationX", x);
        objectAnimatorX.setDuration(2000);
        objectAnimatorX.start();
        objectAnimatorX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Log.d(TAG, "onAnimationUpdate -- " + view.getTranslationX());

            }
        });
        objectAnimatorX.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.d(TAG, "onAnimationEnd -- " + view.getTranslationX());
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }


    private void trySyncAnimation(final View tView, final View sView) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startPopsAnimTrans(tView);
            }
        }, 2000);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startPopsAnimTrans(sView);
            }
        }, 3000);
    }

}
