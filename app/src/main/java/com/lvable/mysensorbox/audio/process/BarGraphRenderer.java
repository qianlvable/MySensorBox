package com.lvable.mysensorbox.audio.process;

/*
 * Modified by Steelkiwi Development, Julia Zudikova
 */

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

/**
 * Copyright 2011, Felix Palmer
 * <p/>
 * Licensed under the MIT license:
 * http://creativecommons.org/licenses/MIT/
 */
public class BarGraphRenderer extends Renderer {
    private int mDivisions;
    private Paint mPaint;
    private boolean mTop;

    /**
     * Renders the FFT data as a series of lines, in histogram form
     *
     * @param divisions - must be a power of 2. Controls how many lines to draw
     * @param paint     - Paint to draw lines with
     * @param top       - whether to draw the lines at the top of the canvas, or the bottom
     */
    public BarGraphRenderer(int divisions, Paint paint, boolean top) {
        super();
        mDivisions = divisions;
        mPaint = paint;
        mTop = top;
    }
    @Override
    public void onRender(Canvas canvas, FFTData data, Rect rect) {
        for (int i = 1; i < data.getBytes().length / mDivisions; i++) {

            mFFTPoints[i * 4] = i * 4 * mDivisions;     // x0
            mFFTPoints[i * 4 + 2] = mFFTPoints[i * 4]; // x1

            byte rfk = data.getBytes()[mDivisions * i];
            byte ifk = data.getBytes()[mDivisions * i + 1];
            float magnitude = (rfk * rfk + ifk * ifk);
            int dbValue = (int) (10 * Math.log10(magnitude));


            if (mTop) {
                mFFTPoints[i * 4 + 1] = 0;                  // y0
                mFFTPoints[i * 4 + 3] = (dbValue * 4 - 20); // y1
            } else {
                mFFTPoints[i * 4 + 1] = rect.height();
                mFFTPoints[i * 4 + 3] = rect.height() - (dbValue * 8 - 10);
            }
        }
        canvas.drawLines(mFFTPoints, mPaint);


    }
}
