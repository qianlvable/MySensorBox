package com.lvable.mysensorbox;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.util.Random;

/**
 * Created by Jiaqi Ning on 8/4/2015.
 */
public class BallSurfaceView extends SurfaceView implements Runnable,AccelerometerActivity.AccelerateChangeListener {
    private Thread mRenderThread = null;
    private SurfaceHolder mHolder;
    private volatile boolean running = false;
    private volatile boolean isFirst = true;
    private Random mRandom;
    private Paint mMoverPaint = new Paint();
    private Paint mAttractorPaint = new Paint();
    private Paint mTextPaint;
    private Attractor mAttractorBall;
    private Mover[] mMovers = new Mover[3];
    private PVector mAccel = new PVector(0,0);
    private StringBuffer sb = new StringBuffer();
    private Bitmap infoBtn;
    private int width = 0;
    private int height = 0;

    public BallSurfaceView(Context context) {
        super(context);

        init();
    }

    private void init() {
        mHolder = getHolder();
        mMoverPaint.setAntiAlias(true);
        mMoverPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mMoverPaint.setColor(Color.YELLOW);

        mAttractorPaint.setAntiAlias(true);
        mAttractorPaint.setColor(0xfff7db08);
        mRandom = new Random();
        mMoverPaint.setStrokeWidth(2);
        mAttractorBall = new Attractor(getWidth()/2,getHeight()/2);
        for (int i =0;i < mMovers.length;i++){
            float m = (float) (Math.random()*1+0.33);
            mMovers[i] = new Mover(m,0,0);
            mMovers[i].color = Color.rgb(mRandom.nextInt(255), mRandom.nextInt(255), mRandom.nextInt(255));
        }

        mTextPaint = new Paint();
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(50);
        mTextPaint.setAntiAlias(true);

        infoBtn = BitmapFactory.decodeResource(getResources(),R.drawable.info_icon2);
    }

    public void resume(){
        running = true;
        mRenderThread = new Thread(this);
        mRenderThread.start();
    }

    @Override
    public void run() {
        while (running){
            if (!mHolder.getSurface().isValid())
                continue;
            Canvas canvas = mHolder.lockCanvas();
            width = canvas.getWidth();
            height = canvas.getHeight();
            canvas.drawRGB(159, 134, 125);
            canvas.drawBitmap(infoBtn,width*0.85f,height*0.05f,null);
            if (isFirst){
                if (mHolder.getSurface().isValid()){
                    for (int i =0;i < mMovers.length;i++){
                        float x = (mRandom.nextInt(width));
                        float y = (mRandom.nextInt(height));
                        mMovers[i].location.x = x;
                        mMovers[i].location.y = y;
                    }
                    mAttractorBall.setLocation(width/2,height/2);
                }
                isFirst = false;
                sb.append("X : 0 Y : 0 Z : 0");
                canvas.drawText(sb.toString(), width / 2, height * 0.8f, mTextPaint);

            }

            for (int i =0;i < mMovers.length;i++){
                PVector force = mAttractorBall.attract(mMovers[i]);
                mMovers[i].applyForce(force);
                mMovers[i].update();

                mMoverPaint.setColor(mMovers[i].color);
                canvas.drawCircle(mMovers[i].location.x,mMovers[i].location.y,mMovers[i].mass * 40, mMoverPaint);
            }

            mAttractorBall.applyForce(mAccel);
            mAttractorBall.update();
            if (mAttractorBall.location.x > width) {
                mAttractorBall.location.x = width;
                mAttractorBall.velocity.mult(0);
            }
            if (mAttractorBall.location.y > height) {
                mAttractorBall.location.y = height;
                mAttractorBall.velocity.mult(0);
            }
            if (mAttractorBall.location.x < 0) {
                mAttractorBall.location.x = 0;
                mAttractorBall.velocity.mult(0);
            }
            if (mAttractorBall.location.y <0) {
                mAttractorBall.location.y = 0;
                mAttractorBall.velocity.mult(0);
            }
            canvas.drawCircle(mAttractorBall.location.x, mAttractorBall.location.y,
                    mAttractorBall.mass, mAttractorPaint);
            canvas.drawText(sb.toString(), width / 2, height * 0.8f, mTextPaint);

            mHolder.unlockCanvasAndPost(canvas);
        }
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
                Toast.makeText(getContext(), "AHa", Toast.LENGTH_SHORT).show();
            }
        }
        return true;
    }


    public AccelerometerActivity.AccelerateChangeListener getListener() {
        return this;
    }

    @Override
    public void dataChange(float dx, float dy,float dz) {
        mAccel.x = -dx*1.2f;
        mAccel.y = dy*1.2f;
        sb.setLength(0);

        sb.append(String.format("X : %.2f Y : %.2f Z : %.2f",dx,dy,dz));
    }
}
