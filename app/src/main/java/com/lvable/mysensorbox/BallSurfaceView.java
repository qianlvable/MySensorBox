package com.lvable.mysensorbox;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;

/**
 * Created by Jiaqi Ning on 8/4/2015.
 */
public class BallSurfaceView extends SurfaceView implements Runnable {
    private Thread mRenderThread = null;
    private SurfaceHolder mHolder;
    private volatile boolean running = false;
    private Random mRandom;
    private Paint mMoverPaint = new Paint();
    private Paint mAttractorPaint = new Paint();
    private Attractor mAttractorBall;
    private Mover[] mMovers = new Mover[5];
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
        mAttractorPaint.setColor(Color.BLUE);
        mRandom = new Random();
        mMoverPaint.setStrokeWidth(2);
        mAttractorBall = new Attractor(getWidth()/2,getHeight()/2);
        for (int i =0;i < mMovers.length;i++){
            float x = (mRandom.nextInt(80));
            Log.d("ball", "x: " + x);
            float y = (mRandom.nextInt(80));
            float m = (float) (Math.random()*1.9+0.1);
            mMovers[i] = new Mover(m,x,y);
            mMovers[i].color = Color.rgb(mRandom.nextInt(255), mRandom.nextInt(255), mRandom.nextInt(255));

        }
    }

    public void resume(){
        running = true;
        mRenderThread = new Thread(this);
        mRenderThread.start();
    }

    @Override
    public void run() {
        if (running){
            if (mHolder.getSurface().isValid()){
                Canvas canvas = mHolder.lockCanvas();
                int width = canvas.getWidth();
                int height = canvas.getHeight();
                for (int i =0;i < mMovers.length;i++){
                    float x = (mRandom.nextInt(width));
                    float y = (mRandom.nextInt(height));
                    mMovers[i].location.x = x;
                    mMovers[i].location.y = y;

                }
            }
        }
        while (running){
            if (!mHolder.getSurface().isValid())
                continue;
            Canvas canvas = mHolder.lockCanvas();
            int width = canvas.getWidth();
            int height = canvas.getHeight();
            canvas.drawRGB(0, 0, 0);
            mAttractorBall.setLocation(width/2,height/2);
            for (int i =0;i < mMovers.length;i++){
                PVector force = mAttractorBall.attract(mMovers[i]);
                mMovers[i].applyForce(force);
                mMovers[i].update();

                mMoverPaint.setColor(mMovers[i].color);

                canvas.drawCircle(mMovers[i].location.x,mMovers[i].location.y,mMovers[i].mass * 40, mMoverPaint);
            }
            canvas.drawCircle(mAttractorBall.location.x,mAttractorBall.location.y,40,mAttractorPaint);
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
}
