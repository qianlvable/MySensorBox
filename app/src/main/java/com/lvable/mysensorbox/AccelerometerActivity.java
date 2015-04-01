package com.lvable.mysensorbox;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lvable.mysensorbox.sensor.framework.util.AccelerometerProvider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;


public class AccelerometerActivity extends ActionBarActivity implements SensorEventListener{
    private SensorManager mSensorManager;
    private Sensor mAccelSensor;
    private float mAccelX = 0;
    private float mAccelY = 0;
    private float mAccelZ = 0;
    private long lastUpdateTime;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer);
        mSensorManager = MyApplication.getInstance().getSensorManager();
        mAccelSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);



    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this,mAccelSensor,SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        long curTime = System.currentTimeMillis();
        if ((curTime - lastUpdateTime)>20) {
            mAccelX = sensorEvent.values[0];
            mAccelY = sensorEvent.values[1];
            mAccelZ = sensorEvent.values[2];




            lastUpdateTime = curTime;
        }


    }




    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
