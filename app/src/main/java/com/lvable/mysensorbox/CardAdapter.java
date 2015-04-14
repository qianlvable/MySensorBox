package com.lvable.mysensorbox;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by Jiaqi Ning on 24/3/2015.
 */
public class CardAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private ArrayList<IconData> mIconDataSet;
    public CardAdapter(ArrayList dataSet,CardOnClickListener listener){
        mIconDataSet = dataSet;
        mOnClickListener = listener;
    }
    private CardOnClickListener mOnClickListener;
    public interface CardOnClickListener{
        public void onClick(int clickColorID,int[] pos,int itemId);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_item,viewGroup,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, final int i) {

        final int bgColor = mIconDataSet.get(i).getBgColor();
        myViewHolder.mTitleTextView.setText(mIconDataSet.get(i).getTitleStringId());
        myViewHolder.mImageView.setImageResource(mIconDataSet.get(i).getImgID());
        myViewHolder.mImageView.setBackgroundColor(Color.TRANSPARENT);
        myViewHolder.mCardLayout.setBackgroundColor(mIconDataSet.get(i).getBgColor());
        myViewHolder.mTitleTextView.setBackgroundColor(mIconDataSet.get(i).getTitleBarColor());
        myViewHolder.mCardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int[] location = new int[2];
                view.getLocationOnScreen(location);
                mOnClickListener.onClick(bgColor,location,i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mIconDataSet.size();
    }


}
