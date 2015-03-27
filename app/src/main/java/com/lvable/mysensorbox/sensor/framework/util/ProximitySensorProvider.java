package com.lvable.mysensorbox.sensor.framework.util;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.util.Log;

import com.lvable.mysensorbox.MyApplication;

/**
 * Created by Jiaqi Ning on 26/3/2015.
 */
public class ProximitySensorProvider extends SensorProvider {
    private SensorManager mSensorManager;
    private Sensor mProximity;

    private float mDistance;

    public ProximitySensorProvider(){
        mSensorManager = MyApplication.getInstance().getSensorManager();
        mProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        Log.d("distance","sensor max range is "+mProximity.getMaximumRange());
    }

    public void startSensor(){
        mSensorManager.registerListener(this,mProximity,SensorManager.SENSOR_DELAY_FASTEST);
    }

    public void stopSensor(){
        mSensorManager.unregisterListener(this);
    }

    @Override
    public float getMaxRange() {
        return mProximity.getMaximumRange();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        mDistance = sensorEvent.values[0];
        Log.d("distance","distance: "+mDistance);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public float getDistance() {
        return mDistance;
    }
}
