package com.lvable.mysensorbox.sensor.framework.util;

import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.hardware.Sensor;
import android.util.Log;

import com.lvable.mysensorbox.MyApplication;

/**
 * Created by Jiaqi Ning on 27/3/2015.
 */
public class LightSensorProvider extends SensorProvider {
    private SensorManager mSensorManager;
    private Sensor mLightSensor;


    private float mLumino;

    public LightSensorProvider(){
        mSensorManager = MyApplication.getInstance().getSensorManager();
        mLightSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
    }

    @Override
    public void startSensor() {
        mSensorManager.registerListener(this,mLightSensor,SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    public void stopSensor() {
        mSensorManager.unregisterListener(this);
    }

    @Override
    public float getMaxRange() {
        return mLightSensor.getMaximumRange();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType()==Sensor.TYPE_LIGHT) {
            mLumino = sensorEvent.values[0];
            Log.d("light", "the light is " + mLumino);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public float getLumino() {
        return mLumino;
    }
}
