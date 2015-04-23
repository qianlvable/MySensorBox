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
import android.view.ViewGroup;
import android.widget.RelativeLayout;


public class DistanceActivity extends ActionBarActivity implements SensorEventListener,SurfaceViewInfoBtnClickListener{
    private SensorManager mSensorManager;
    private Sensor mProximitySensor;
    private float mCurDistance;
    private TreeSurfaceView mRenderView;
    private Toolbar mToolbar;
    private float maxRange;
    private float preDistance;
    private AlertDialog mDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRenderView = new TreeSurfaceView(this,this);
        setContentView(R.layout.activity_distance);

        mToolbar = (Toolbar)findViewById(R.id.toolbar_distance);
        mToolbar.setLogo(R.drawable.logo);
        mToolbar.setTitle("Distance sensor");

        RelativeLayout layout = (RelativeLayout)findViewById(R.id.distance_layout);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                ,ViewGroup.LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.BELOW, R.id.toolbar_distance);
        layout.addView(mRenderView, params);

        mSensorManager = MyApplication.getInstance().getSensorManager();
        mProximitySensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        maxRange = mProximitySensor.getMaximumRange();
        mDialog = OtherUtils.getInfoDialog(this, mProximitySensor);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRenderView.resume();
        mSensorManager.registerListener(this, mProximitySensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mRenderView.pause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (TreeSurfaceView.isAnimationFinish) {
            TreeSurfaceView.C2Fcount = 0;
            TreeSurfaceView.F2Ccount = 20;
            mCurDistance = sensorEvent.values[0];
            Log.d("distance",mCurDistance + "");
            if (preDistance >= maxRange) {
                if ((mCurDistance - preDistance) < 0)
                    mRenderView.setState(TreeSurfaceView.STATE.FAR_CLOESD);
                else
                    mRenderView.setState(TreeSurfaceView.STATE.FAR);
            } else {
                if ((mCurDistance - preDistance) > 0)
                    mRenderView.setState(TreeSurfaceView.STATE.CLOSED_FAR);
                else
                    mRenderView.setState(TreeSurfaceView.STATE.CLOSED);
            }
            preDistance = mCurDistance;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {


    }

    @Override
    public void onInfoBtnClick() {
        mDialog.show();
    }
}
