package com.lvable.mysensorbox.audio.process;

/**
 * Modified by Jiaqi Ning
 */

/**
 * Copyright 2011, Felix Palmer
 *
 * Licensed under the MIT license:
 * http://creativecommons.org/licenses/MIT/
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.media.audiofx.Visualizer;
import android.util.AttributeSet;
import android.view.View;

import java.util.HashSet;
import java.util.Set;

public class VisualizerView extends View{

    private byte[] mFFTBytes;

    private FFTData mFftData = null;
    private Rect mRect = new Rect();
    private Matrix mMatrix = new Matrix();



    private Set<Renderer> mRenderers;

    private Paint mFadePaint = new Paint();


    Bitmap mCanvasBitmap;
    Canvas mCanvas;

    public VisualizerView(Context context) {
        this(context, null, 0);
    }

    public VisualizerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VisualizerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mFFTBytes = null;

        // TODO: desgin a better metho to change alpha
        mFadePaint.setColor(Color.argb(240, 255, 255, 255)); // Adjust alpha to change how quickly the image fades
        mFadePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY));

        mRenderers = new HashSet<Renderer>();
    }

    public void addRenderer(Renderer renderer) {
        if (renderer != null) {
            mRenderers.add(renderer);
        }
    }

    public void clearRenderers() {
        mRenderers.clear();
    }

    /**
     * Pass FFT data to the visualizer. Typically this will be obtained from the
     * Android Visualizer.OnDataCaptureListener call back. See
     * {@link Visualizer.OnDataCaptureListener#onFftDataCapture }
     * @param bytes
     */
    public void updateVisualizerFFT(byte[] bytes) {
        mFFTBytes = bytes;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Create canvas once we're ready to draw
        mRect.set(0, 0, getWidth(), getHeight());

        if (mCanvasBitmap == null) {
            mCanvasBitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
        }
        if (mCanvas == null) {
            mCanvas = new Canvas(mCanvasBitmap);
        }

        mCanvas.drawColor(Color.TRANSPARENT);



        if (mFFTBytes != null) {
            // Render all FFT renderers
            if (mFftData == null) {
                mFftData = new FFTData();
            }
            mFftData.setBytes(mFFTBytes);
            for (Renderer r : mRenderers) {
                r.render(mCanvas, mFftData, mRect);
            }
        }

        // Fade out old contents
        mCanvas.drawPaint(mFadePaint);


        mMatrix.reset();
        canvas.drawBitmap(mCanvasBitmap, mMatrix, null);
    }

}
