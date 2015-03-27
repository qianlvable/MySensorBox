package com.lvable.mysensorbox;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Jiaqi Ning on 24/3/2015.
 */
public class MyViewHolder extends RecyclerView.ViewHolder{
    public ImageView mImageView;
    public TextView mTitleTextView;
    public View mCardLayout;
    public MyViewHolder(View itemView) {
        super(itemView);
        mImageView = (ImageView)itemView.findViewById(R.id.icon_img);
        mTitleTextView = (TextView)itemView.findViewById(R.id.bottom_text);
        mCardLayout = itemView.findViewById(R.id.card_layout);
    }
}