package com.lvable.mysensorbox;

import android.app.Application;
import android.content.Context;
import android.hardware.SensorManager;

/**
 * Created by Jiaqi Ning on 26/3/2015.
 */
public class MyApplication extends Application {

    private SensorManager mSensorManager;
    private static MyApplication mInstance;

    public static synchronized MyApplication getInstance(){
        return mInstance;
    }

    public SensorManager getSensorManager() {
        if(mSensorManager == null){
            mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        }
        return mSensorManager;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }
}
