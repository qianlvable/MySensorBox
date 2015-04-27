package com.lvable.mysensorbox;

import android.app.AlertDialog;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;




public class LightActivity extends ActionBarActivity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mLightSensor;
    private float mLumino;
    private float maxRange;
    private Toolbar mToolbar;
    private ImageView mLightImageView;
    private TextView mTextViewValue;
    private ImageButton mInfoBtn;
    private AlertDialog mDialog;

    private int[] imageResId = {R.drawable.light1,R.drawable.light2,
            R.drawable.light3,R.drawable.light4,R.drawable.light4};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_light);

        mSensorManager = MyApplication.getInstance().getSensorManager();
        mLightSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        maxRange = mLightSensor.getMaximumRange() / 5;

        mInfoBtn = (ImageButton)findViewById(R.id.info_btn);
        mTextViewValue = (TextView)findViewById(R.id.tv_light_val);
        mLightImageView = (ImageView)findViewById(R.id.iv_light);
        mToolbar = (Toolbar)findViewById(R.id.toolbar_light);
        setupToolbar();


        mDialog = OtherUtils.getInfoDialog(this,mLightSensor,getString(R.string.light_extra_content));


        mInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mDialog.show();
            }
        });

    }

    private void setupToolbar() {
        mToolbar.setLogo(R.drawable.logo);
        mToolbar.setTitle(getString(R.string.toolbar_light_title));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mLightSensor, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        mLumino = sensorEvent.values[0];
        mTextViewValue.setText(mLumino+" Lu");
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

       // Log.d("light", "the light is " + mLumino);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }




}
