package com.lvable.mysensorbox;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
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


public class AccelerometerActivity extends ActionBarActivity implements SensorEventListener
        ,SurfaceViewInfoBtnClickListener{
    private SensorManager mSensorManager;
    private Sensor mAccelSensor;
    private float mAccelX = 0;
    private float mAccelY = 0;
    private float mAccelZ = 0;
    private long lastUpdateTime;
    private BallSurfaceView renderView;
    private Toolbar mToolbar;
    private AccelerateChangeListener mAccelerateChangeListener;
    private AlertDialog mDialog;



    public interface AccelerateChangeListener{
        void dataChange(float dx,float dy,float dz);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer);
        renderView = new BallSurfaceView(this,this);
        mToolbar = (Toolbar)findViewById(R.id.toolbar_accel);
        mToolbar.setLogo(R.drawable.logo);
        mToolbar.setTitle("Acceleration sensor");
        int color = 0xff4CAF50;
        mToolbar.setBackgroundColor(color);
        FrameLayout layout = (FrameLayout)findViewById(R.id.accel_layout);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                ,ViewGroup.LayoutParams.MATCH_PARENT);
        layout.addView(renderView, params);

        mSensorManager = MyApplication.getInstance().getSensorManager();
        mAccelSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mAccelerateChangeListener = renderView.getListener();

        mDialog = OtherUtils.getInfoDialog(this,mAccelSensor);

    }

    @Override
    protected void onResume() {
        super.onResume();
        renderView.resume();
        mSensorManager.registerListener(this,mAccelSensor,SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        renderView.pause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        long curTime = System.currentTimeMillis();
        if ((curTime - lastUpdateTime)>100) {
            mAccelX = sensorEvent.values[0];
            mAccelY = sensorEvent.values[1];
            mAccelZ = sensorEvent.values[2];
            lastUpdateTime = curTime;
            Log.d("accel", mAccelX + "," + mAccelY + "," + mAccelZ);
            if (Math.abs(mAccelX) < 0.25 && Math.abs(mAccelY) < 0.25)
                return;
            mAccelerateChangeListener.dataChange(mAccelX,mAccelY,mAccelZ);
        }


    }


    @Override
    public void onInfoBtnClick() {
        mDialog.show();
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
