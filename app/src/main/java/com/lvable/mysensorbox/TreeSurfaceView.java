package com.lvable.mysensorbox;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

/**
 * Created by Jiaqi Ning on 13/4/2015.
 */
public class TreeSurfaceView extends SurfaceView implements Runnable {
    private Thread mRenderThread = null;
    private SurfaceHolder mHolder;
    private volatile boolean running = false;
    private Paint mPaint;
    private float degrees = 0;
    static int F2Ccount = 20;
    static int C2Fcount = 0;

    enum STATE {CLOSED_FAR,FAR_CLOESD,CLOSED,FAR};
    static boolean isAnimationFinish = true;
    private float strokeWidth = 2.6f;

    private STATE mState;
    private STATE mPreState;


    boolean isHasData = false;


    public TreeSurfaceView(Context context) {
        super(context);
        init();
    }

    private void init() {
        mHolder = getHolder();
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(strokeWidth);
        mPaint.setAntiAlias(true);

        mState = STATE.CLOSED_FAR;
        mPreState = STATE.CLOSED;

    }


    @Override
    public void run() {
        while (running){
            if (!mHolder.getSurface().isValid())
                continue;
            if (mState != mPreState || !isAnimationFinish) {
                Canvas canvas = mHolder.lockCanvas();
                int width = canvas.getWidth();
                int height = canvas.getHeight();

                switch (mState) {
                    case CLOSED:
                        degrees = 10;
                        mPreState = STATE.CLOSED;
                        break;
                    case FAR:
                        degrees = 70;
                        mPreState = STATE.FAR;
                        break;
                    case CLOSED_FAR:
                        isAnimationFinish = false;
                        if (C2Fcount <= 20) {
                            degrees = mapValue(C2Fcount, 0, 20, 0, 90);
                            C2Fcount++;
                        } else {
                            isAnimationFinish = true;
                        }
                        mPreState = STATE.CLOSED_FAR;
                        break;
                    case FAR_CLOESD:
                        isAnimationFinish = false;
                        if (F2Ccount >= 0) {
                            degrees = mapValue(F2Ccount, 0, 20, 0, 90);
                            F2Ccount--;
                        } else {
                            isAnimationFinish = true;
                        }
                        mPreState = STATE.FAR_CLOESD;
                        break;
                }

                canvas.drawRGB(102, 187, 106);
                canvas.translate(width / 2, height);
                branch(canvas, (float) (width * 0.3));
                isHasData = true;

                mHolder.unlockCanvasAndPost(canvas);
            }
        }
    }

    void branch(Canvas canvas,float len){

        canvas.drawLine(0, 0, 0, -len, mPaint);
        canvas.translate(0, -len);

        len *= 0.66;

        if (len > 2){

            canvas.save();

            canvas.rotate(degrees);
            branch(canvas, len);
            canvas.restore();

            canvas.save();
            canvas.rotate(-degrees);
            branch(canvas, len);
            canvas.restore();
        }

    }

    public void resume(){
        running = true;
        mRenderThread = new Thread(this);
        mRenderThread.start();
    }

    public void pause(){
        running = false;
        while (true){
            try {
                mRenderThread.join();
                return;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public float mapValue(float val,float start,float end,float lower,float upper) {
        if (start >= end)
            return -999;
        return Math.abs((val - start) / ( end - start)) * (upper - lower) + lower;
    }

    public void setState(STATE state) {
        mState = state;
    }
}
