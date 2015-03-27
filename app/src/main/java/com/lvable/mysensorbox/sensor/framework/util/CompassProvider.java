package com.lvable.mysensorbox.sensor.framework.util;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import com.lvable.mysensorbox.MyApplication;

/**
 * Created by Jiaqi Ning on 27/3/2015.
 */
public class CompassProvider extends SensorProvider {
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mMagnetometer;

    private float[] mGravity;
    private float[] mMagnetic;

    public CompassProvider(){
        mSensorManager = MyApplication.getInstance().getSensorManager();
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

    }
    @Override
    public void startSensor() {
        mSensorManager.registerListener(this,mAccelerometer,SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(this,mMagnetometer,SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void stopSensor() {
     //   mSensorManager.unregisterListener(this,mAccelerometer);
        mSensorManager.unregisterListener(this,mMagnetometer);

    }

    @Override
    public float getMaxRange() {
        return 0;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if (sensorEvent.sensor == mAccelerometer){
            mGravity = sensorEvent.values.clone();
        }else if (sensorEvent.sensor == mMagnetometer) {
            mMagnetic = sensorEvent.values.clone();
            Log.d("compass","magnet change");
        }

        if(mGravity != null && mMagnetic != null) {
            updateDirection();
        }

    }

    private void updateDirection() {
        float[] temp = new float[9];
        float[] rotation = new float[9];
        //Load rotation matrix into R
        SensorManager.getRotationMatrix(temp, null, mGravity, mMagnetic);
        //Remap to camera's point-of-view
        SensorManager.remapCoordinateSystem(temp, SensorManager.AXIS_X, SensorManager.AXIS_Z, rotation);
        //Return the orientation values
        float[] values = new float[3];
        SensorManager.getOrientation(rotation, values);
        //Convert to degrees
        for (int i=0; i < values.length; i++) {
            Double degrees = (values[i] * 180) / Math.PI;
            values[i] = degrees.floatValue();
        }
        Log.d("compass","changed");

        Log.d("compass",getDirectionFromDegrees(values[0]));
        Log.d("compass",String.format("Azimuth: %1$1.2f, Pitch: %2$1.2f, Roll: %3$1.2f",
                values[0], values[1], values[2]));

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private String getDirectionFromDegrees(float degrees) {
        if(degrees >= -22.5 && degrees < 22.5) { return "N"; }
        if(degrees >= 22.5 && degrees < 67.5) { return "NE"; }
        if(degrees >= 67.5 && degrees < 112.5) { return "E"; }
        if(degrees >= 112.5 && degrees < 157.5) { return "SE"; }
        if(degrees >= 157.5 || degrees < -157.5) { return "S"; }
        if(degrees >= -157.5 && degrees < -112.5) { return "SW"; }
        if(degrees >= -112.5 && degrees < -67.5) { return "W"; }
        if(degrees >= -67.5 && degrees < -22.5) { return "NW"; }

        return null;
    }

}
