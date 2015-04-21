package com.lvable.mysensorbox;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.lvable.mysensorbox.sensor.framework.util.LightSensorProvider;


public class LightActivity extends ActionBarActivity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mLightSensor;
    private float mLumino;
    private float maxRange;
    private Toolbar mToolbar;
    private ImageView mLightImageView;
    private int[] imageResId = {R.drawable.light1,R.drawable.light2,
            R.drawable.light3,R.drawable.light4,R.drawable.light4};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light);

        mSensorManager = MyApplication.getInstance().getSensorManager();
        mLightSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        // TODO: add check sensor available
        maxRange = mLightSensor.getMaximumRange() / 5;

        mLightImageView = (ImageView)findViewById(R.id.iv_light);
        mToolbar = (Toolbar)findViewById(R.id.toolbar_light);
        mToolbar.setLogo(R.drawable.logo);
        mToolbar.setTitle("Light sensor");
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this,mLightSensor,SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        mLumino = sensorEvent.values[0];

        if( mLumino < 50){
            mLightImageView.setImageResource(imageResId[0]);
        }else if ( 50 <= mLumino && mLumino < 90){
            mLightImageView.setImageResource(imageResId[1]);
        }else if ( 90 <= mLumino && mLumino < 120){
            mLightImageView.setImageResource(imageResId[2]);
        }else if ( 120 <= mLumino && mLumino < 500){
            mLightImageView.setImageResource(imageResId[3]);
        }else{
            mLightImageView.setImageResource(imageResId[4]);
        }

        Log.d("light", "the light is " + mLumino);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }




}
