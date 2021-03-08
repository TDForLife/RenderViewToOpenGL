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
import android.view.View;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

public class GLActivity extends Activity {

    private static final String TAG = "activity";

    private FrameLayout mContainer;
    private GLSurfaceView mDrawSurfaceView;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContainer = findViewById(R.id.container_layout);
        mDrawSurfaceView = findViewById(R.id.draw_surface_view);
        mHandler = new Handler();
//        addTextView();
//        addGLViewGroup();
//        addProgressView();
        activeFixedTextView();
    }

    private void activeFixedTextView() {
        final GLTextView fixedTextView = findViewById(R.id.fixed_gl_tv);
        final ViewRenderer renderer = new ViewRenderer(getApplicationContext(), fixedTextView);

        mDrawSurfaceView.setVisibility(View.VISIBLE);
        mDrawSurfaceView.setEGLContextClientVersion(2);
        mDrawSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        mDrawSurfaceView.setRenderer(renderer);
        // 如果是 WHEN_DIRTY 的绘制模式，那么 SurfaceView draw 的时候并没有 fixedTextView 的图像数据，而 draw 又只执行一次
        // 这便导致 SurfaceView 画不出内容，所以 SurfaceView 要有内容要两个步骤，一是目标视图已经有数据了，二是 SurfaceView 在
        // 一的前提下会再触发 draw 方法，即 GLSurfaceView 的 onDrawFrame
        // mDrawSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

//        fixedTextView.post(new Runnable() {
//            @Override
//            public void run() {
//                renderer.transformWorldCoords(fixedTextView);
//            }
//        });

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                fixedTextView.invalidate();
            }
        }, 2500);
    }

    private void addTextView() {
        final GLTextView glTextView = new GLTextView(this);
        glTextView.setText("Hello TextView");
        glTextView.setTextColor(Color.WHITE);

        ViewRenderer renderer = new ViewRenderer(getApplicationContext(), glTextView);
        mDrawSurfaceView.setVisibility(View.VISIBLE);
        mDrawSurfaceView.setEGLContextClientVersion(2);
        mDrawSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        mDrawSurfaceView.setRenderer(renderer);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mContainer.addView(glTextView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
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
        final GLTextView glTextView = new GLTextView(this);
        glTextView.setText("Hello GLViewGroup");
        glTextView.setTextColor(Color.WHITE);
        final GLLinearLayout glLinearLayout = new GLLinearLayout(this);
        glLinearLayout.setWillNotDraw(false);
        glLinearLayout.addView(glTextView);

        // Setup the surface view for drawing to
        ViewRenderer renderer = new ViewRenderer(getApplicationContext(), glLinearLayout);
        final GLSurfaceView glSurfaceView = new GLSurfaceView(getApplicationContext());
        glSurfaceView.setEGLContextClientVersion(2);
        glSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        glSurfaceView.setRenderer(renderer);

        final FrameLayout.LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mContainer.addView(glSurfaceView, layoutParams);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mContainer.addView(glLinearLayout, layoutParams);
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


    private void addProgressView() {
        final GLProgressBar glProgressBar = new GLProgressBar(this);
        ViewRenderer renderer = new ViewRenderer(getApplicationContext(), glProgressBar);
        mDrawSurfaceView.setVisibility(View.VISIBLE);
        mDrawSurfaceView.setEGLContextClientVersion(2);
        mDrawSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        mDrawSurfaceView.setRenderer(renderer);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mContainer.addView(glProgressBar, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            }
        }, 100);
    }

    /******************************************* 工具分界 *****************************************************/

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
