package com.lvable.mysensorbox;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Jiaqi Ning on 13/4/2015.
 */
public class TreeSurfaceView extends SurfaceView implements Runnable {
    private Thread mRenderThread = null;
    private SurfaceHolder mHolder;
    private volatile boolean running = false;
    private Paint mPaint;
    private Paint mTextPaint;
    private float degrees = 0;
    static int F2Ccount = 20;
    static int C2Fcount = 0;

    enum STATE {CLOSED_FAR,FAR_CLOESD,CLOSED,FAR};
    static boolean isAnimationFinish = true;
    private float strokeWidth = 2.6f;

    private STATE mState;
    private STATE mPreState;
    private Bitmap infoBtn;

    private int width = 0;
    private int height = 0;

    boolean isHasData = false;

    private SurfaceViewInfoBtnClickListener mOnInfoClickListener;

    public TreeSurfaceView(Context context,SurfaceViewInfoBtnClickListener listener) {
        super(context);
        mOnInfoClickListener = listener;
        init();
    }

    private void init() {
        mHolder = getHolder();
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(strokeWidth);
        mPaint.setAntiAlias(true);

        mTextPaint = new Paint();
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(60);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        mState = STATE.CLOSED_FAR;
        mPreState = STATE.CLOSED;

        infoBtn = BitmapFactory.decodeResource(getResources(), R.drawable.info_icon2);
    }


    @Override
    public void run() {
        while (running){
            if (!mHolder.getSurface().isValid())
                continue;


            if (mState != mPreState || !isAnimationFinish) {
                Canvas canvas = mHolder.lockCanvas();
                canvas.drawRGB(102, 187, 106);
                width = canvas.getWidth();
                height = canvas.getHeight();

                canvas.drawBitmap(infoBtn,width*0.85f,height*0.05f,null);

                if (mState == STATE.FAR || mState == STATE.CLOSED_FAR)
                    canvas.drawText("Far",width/2,height*0.1f,mTextPaint);
                else
                    canvas.drawText("Closed",width/2,height*0.1f,mTextPaint);

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


                canvas.translate(width/2, height);
                branch(canvas, (float) (width * 0.35));
                isHasData = true;

                mHolder.unlockCanvasAndPost(canvas);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            float x = event.getX();
            float y = event.getY();
            float bitmapLX = width*0.85f;
            float bitmapRX = bitmapLX + infoBtn.getWidth();
            float bitmapTop = height*0.05f;
            float bitmapLow = bitmapTop + infoBtn.getHeight();
            if ( x > bitmapLX && x < bitmapRX && y > bitmapTop && y < bitmapLow){
                mOnInfoClickListener.onInfoBtnClick();
            }
        }
        return true;
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
