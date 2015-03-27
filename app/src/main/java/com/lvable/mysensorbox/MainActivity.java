package com.lvable.mysensorbox;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;

import io.codetail.animation.SupportAnimator;


public class MainActivity extends ActionBarActivity implements CardAdapter.CardOnClickListener{
    private RecyclerView mRecyclerView;
    private View mRevealView;
    private Toolbar mToolbar;
    private ArrayList<IconData> mIconDatas;
    private int clickedItemId;

    @Override
    protected void onResume() {
        super.onResume();
        mRevealView.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadIconDatas();


        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(mToolbar);
        mToolbar.setLogo(R.drawable.ic_launcher);
        mToolbar.setSubtitle("Hi,Nick");

        mRecyclerView = (RecyclerView)findViewById(R.id.grid_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        mRecyclerView.setAdapter(new CardAdapter(mIconDatas,this));

        mRevealView = findViewById(R.id.reveal_view);



    }

    private void loadIconDatas() {
        mIconDatas = new ArrayList<IconData>();

        mIconDatas.add(new IconData(R.string.leader_board,R.drawable.c1,
                getResources().getColor(R.color.light_white),getResources().getColor(R.color.light_gray)));
        mIconDatas.add(new IconData(R.string.chemistry,R.drawable.c2,
                getResources().getColor(R.color.light_blue),getResources().getColor(R.color.dark_blue)));
        mIconDatas.add(new IconData(R.string.music,R.drawable.c3,
                getResources().getColor(R.color.light_yellow),getResources().getColor(R.color.dark_yellow)));
        mIconDatas.add(new IconData(R.string.bio,R.drawable.c4,
                getResources().getColor(R.color.light_orange),getResources().getColor(R.color.dark_orange)));
        mIconDatas.add(new IconData(R.string.bio,R.drawable.c5,
                getResources().getColor(R.color.light_brown),getResources().getColor(R.color.dark_brown)));
        mIconDatas.add(new IconData(R.string.add,R.drawable.c6,
                getResources().getColor(R.color.green_yellow),getResources().getColor(R.color.dark_green_yellow)));
    }

    SupportAnimator.AnimatorListener revealAnimationListener = new SupportAnimator.AnimatorListener() {
        @Override
        public void onAnimationStart() {

        }

        @Override
        public void onAnimationEnd() {
            Intent intent;
            switch (clickedItemId){
                case 0:
                    intent = new Intent(MainActivity.this,DistanceActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);

                    break;
                case 1:
                    intent = new Intent(MainActivity.this,LightActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);

                    break;
                case 2:
                    intent = new Intent(MainActivity.this,CompassActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);

                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
            }
        }

        @Override
        public void onAnimationCancel() {

        }

        @Override
        public void onAnimationRepeat() {

        }
    };


    @Override
    public void onClick(int clickColor,int[] touchPos,int itemId) {
        mRevealView.setBackgroundColor(clickColor);
        AnimationUtil.showRevealEffect(mRevealView,touchPos[0],touchPos[1],revealAnimationListener);
        clickedItemId = itemId;
    }
}
