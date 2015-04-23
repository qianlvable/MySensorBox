package com.lvable.mysensorbox;

import android.app.AlertDialog;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


public class CompassActivity extends ActionBarActivity implements SensorEventListener{
    private ImageView mPointer;
    private TextView mDataTextView;
    private SensorManager mSensorManager;
    private Sensor mAccelSensor;
    private Sensor mMagnetSensor;
    private float[] mLastAccelerometer = new float[3];
    private float[] mLastMagnetometer = new float[3];
    private float[] mR = new float[9];
    private float[] mOrientation = new float[3];
    private float mCurrentDegree = 0f;
    private Toolbar mToolbar;
    private AlertDialog mDialog;
    private ImageButton mInfoBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        mInfoBtn = (ImageButton)findViewById(R.id.info_btn_compass);
        mPointer = (ImageView) findViewById(R.id.compass_img);
        mDataTextView = (TextView)findViewById(R.id.tv_amz);
        mToolbar = (Toolbar)findViewById(R.id.toolbar_compass);
        int toolbarColor = 0xff314FA0;
        mToolbar.setBackgroundColor(toolbarColor);
        mToolbar.setTitle("Compass");
        mToolbar.setLogo(R.drawable.logo);

        mDialog = OtherUtils.getInfoDialog(this, mMagnetSensor);

        mInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.show();
            }
        });
    }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelSensor, SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this, mMagnetSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this, mAccelSensor);
        mSensorManager.unregisterListener(this, mMagnetSensor);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == mAccelSensor) {
            System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.length);
        } else if (event.sensor == mMagnetSensor) {
            System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);

        }
        if (mAccelSensor != null && mMagnetSensor != null) {
            SensorManager.getRotationMatrix(mR, null, mLastAccelerometer, mLastMagnetometer);
            SensorManager.getOrientation(mR, mOrientation);
            float azimuthInRadians = mOrientation[0];
            float azimuthInDegress = (float)(Math.toDegrees(azimuthInRadians)+360)%360;
            RotateAnimation ra = new RotateAnimation(
                    mCurrentDegree,
                    -azimuthInDegress,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f);

            ra.setDuration(250);
            ra.setFillAfter(true);
            mPointer.startAnimation(ra);

            mCurrentDegree = -azimuthInDegress;
            mDataTextView.setText(String.format("Azimuth : %.2f",azimuthInDegress));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}
