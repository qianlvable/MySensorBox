package com.lvable.mysensorbox.sensor.framework.util;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;

import com.lvable.mysensorbox.MyApplication;

/**
 * Created by Jiaqi Ning on 28/3/2015.
 */
public class AccelerometerProvider extends SensorProvider{
    private SensorManager mSensorManager;
    private Sensor mAccelSensor;
    private float mAccelX = 0;



    private float mAccelY = 0;
    private float mAccelZ = 0;

    public AccelerometerProvider(){
        mSensorManager = MyApplication.getInstance().getSensorManager();
        mAccelSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

    }
    @Override
    public void startSensor() {
        mSensorManager.registerListener(this,mAccelSensor,SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void stopSensor() {
        mSensorManager.unregisterListener(this);
    }

    @Override
    public float getMaxRange() {
        return 0;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        mAccelX = sensorEvent.values[0];
        mAccelY = sensorEvent.values[1];
        mAccelZ = sensorEvent.values[2];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public float getAccelY() {
        return mAccelY;
    }

    public float getAccelX() {
        return mAccelX;
    }

    public float getAccelZ() {
        return mAccelZ;
    }
}
