package com.lvable.mysensorbox;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by Jiaqi Ning on 23/4/2015.
 */
public class InfoPanelLayout  extends RelativeLayout{
    private GradientDrawable mShadowDrawable = null;
    int[] colors = { Color.argb (0xf2, 0, 0, 0), Color.argb(0, 0, 0, 0)};

    public InfoPanelLayout(Context context) {
        super(context);
    }

    public InfoPanelLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

/*
    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mShadowDrawable == null)
            mShadowDrawable = new GradientDrawable (GradientDrawable.Orientation.BOTTOM_TOP,colors);

        mShadowDrawable.setBounds(0,0,getWidth(),4);
        mShadowDrawable.draw(canvas);
    }*/
}
