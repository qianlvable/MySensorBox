package com.lvable.mysensorbox;

import android.content.Intent;
import android.hardware.Sensor;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.codetail.animation.SupportAnimator;


public class MainActivity extends ActionBarActivity implements CardAdapter.CardOnClickListener{
    private RecyclerView mRecyclerView;
    private View mRevealView;
    private Toolbar mToolbar;
    private ArrayList<IconData> mIconDatas;
    private int clickedItemId;
    private boolean hasDistanceSensor = false;
    private boolean hasAccelSensor = false;
    private boolean hasLightSensor = false;
    private boolean hasMagneticSensor = false;
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
        getWindow().setBackgroundDrawable(null);

        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(mToolbar);
        mToolbar.setLogo(R.drawable.logo);

        mRecyclerView = (RecyclerView)findViewById(R.id.grid_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerView.setAdapter(new CardAdapter(mIconDatas, this));

        mRevealView = findViewById(R.id.reveal_view);

        List<Sensor> sensorList = MyApplication.getInstance().getSensorManager().getSensorList(Sensor.TYPE_ALL);
        for (Sensor s: sensorList){
            if (s.getType() == Sensor.TYPE_ACCELEROMETER)
                hasAccelSensor = true;
            else if (s.getType() == Sensor.TYPE_PROXIMITY)
                hasDistanceSensor = true;
            else if (s.getType() == Sensor.TYPE_LIGHT)
                hasLightSensor = true;
            else if (s.getType() == Sensor.TYPE_MAGNETIC_FIELD)
                hasMagneticSensor = true;
        }

    }

    private void loadIconDatas() {
        mIconDatas = new ArrayList<IconData>();
        mIconDatas.add(new IconData(R.string.light_name,R.drawable.light_icon,
                getResources().getColor(R.color.light_blue),getResources().getColor(R.color.dark_blue)));
        mIconDatas.add(new IconData(R.string.distance_name,R.drawable.ic_distance4,
                getResources().getColor(R.color.light_white),getResources().getColor(R.color.light_gray)));

        mIconDatas.add(new IconData(R.string.sound_name,R.drawable.c3,
                getResources().getColor(R.color.light_yellow),getResources().getColor(R.color.dark_yellow)));
        mIconDatas.add(new IconData(R.string.compass_name,R.drawable.compass_icon,
                getResources().getColor(R.color.light_purple),getResources().getColor(R.color.dark_purple)));
        mIconDatas.add(new IconData(R.string.gravity_name,R.drawable.c5,
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
                    intent = new Intent(MainActivity.this, LightActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    break;
                case 1:
                    intent = new Intent(MainActivity.this, DistanceActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    break;
                case 2:
                    intent = new Intent(MainActivity.this,SoundWaveActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    break;
                case 3:
                    intent = new Intent(MainActivity.this, CompassActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    break;
                case 4:
                    intent = new Intent(MainActivity.this, AccelerometerActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
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

        switch (itemId){
            case 0:
                if (!hasLightSensor) {
                    Toast.makeText(getApplicationContext(), "Sorry,the light sensor is not available!"
                            , Toast.LENGTH_LONG).show();
                    return;
                }
                break;
            case 1:
                if (!hasDistanceSensor) {
                    Toast.makeText(getApplicationContext(), "Sorry,the distance sensor is not available!"
                            , Toast.LENGTH_LONG).show();
                    return;
                }
                break;
            case 2:
                break;
            case 3:
                if (!hasMagneticSensor && !hasAccelSensor){
                    Toast.makeText(getApplicationContext(), "Sorry,the compass is not available!"
                            , Toast.LENGTH_LONG).show();
                    return;
                }
                break;
            case 4:
                if (!hasAccelSensor) {
                    Toast.makeText(getApplicationContext(), "Sorry,the accelerate sensor is not available!"
                            , Toast.LENGTH_LONG).show();
                    return;
                }
                break;
            case 5:
                break;
        }
        mRevealView.setBackgroundColor(clickColor);
        AnimationUtil.showRevealEffect(mRevealView,touchPos[0],touchPos[1],revealAnimationListener);
        clickedItemId = itemId;
    }
}
