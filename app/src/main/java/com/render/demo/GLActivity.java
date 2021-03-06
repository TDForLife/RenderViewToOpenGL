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

public class GLActivity extends Activity {

    private FrameLayout root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the activity to display the glSurfaceView
        setContentView(R.layout.activity_main);
        root = findViewById(R.id.root);
        addGLViewGroup();
    }


    private void addProgressView() {

        Display mDisplay = getWindowManager().getDefaultDisplay();
        final GLProgressBar glProgressBar = new GLProgressBar(this);

        ViewRenderer renderer = new ViewRenderer(getApplicationContext(), glProgressBar, mDisplay);
        GLSurfaceView glSurfaceView = new GLSurfaceView(getApplicationContext());
        // Setup the surface view for drawing to

        glSurfaceView.setEGLContextClientVersion(2);
        glSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        glSurfaceView.setRenderer(renderer);
        //glSurfaceView.setZOrderOnTop(true);
        // Add our WebView to the Android View hierarchy
        glSurfaceView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        root.addView(glSurfaceView);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                root.addView(glProgressBar);
            }
        }, 100);

        //addContentView(glProgressBar, new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    }

    private void addTextView() {
        Display mDisplay = getWindowManager().getDefaultDisplay();
        final GLTextView glTextView = new GLTextView(this);
        glTextView.setText("Hello world");
        glTextView.setTextColor(Color.WHITE);

        ViewRenderer renderer = new ViewRenderer(getApplicationContext(), glTextView, mDisplay);
        final GLSurfaceView glSurfaceView = new GLSurfaceView(getApplicationContext());
        // Setup the surface view for drawing to

        glSurfaceView.setEGLContextClientVersion(2);
        glSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        glSurfaceView.setRenderer(renderer);
        //glSurfaceView.setZOrderOnTop(true);
        // Add our WebView to the Android View hierarchy
        glSurfaceView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        root.addView(glSurfaceView);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                root.addView(glTextView);
            }
        }, 100);
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

        // glSurfaceView.setZOrderOnTop(true);
        // Add our WebView to the Android View hierarchy
        glSurfaceView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        root.addView(glSurfaceView);
        // Add view
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
                root.addView(glLinearLayout, layoutParams);
            }
        }, 100);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                glTextView.setLeft(100);
                startPopsAnimTrans(glLinearLayout);
                glLinearLayout.postInvalidate();
            }
        }, 2000);
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

    // 属性动画-平移
    private void startPopsAnimTrans(final View view) {
        float[] x = {60f};
        ObjectAnimator objectAnimatorX = ObjectAnimator.ofFloat(view, "translationX", x);
        objectAnimatorX.setDuration(2000);
        objectAnimatorX.start();
        objectAnimatorX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Log.d("zwt", "onAnimationUpdate -- " + view.getTranslationX());

            }
        });
        objectAnimatorX.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.d("zwt", "onAnimationEnd -- " + view.getTranslationX());
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

}
