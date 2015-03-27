package com.lvable.mysensorbox;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.lvable.mysensorbox.sensor.framework.util.LightSensorProvider;


public class LightActivity extends ActionBarActivity {
    LightSensorProvider mLightSensorProvider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light);

        mLightSensorProvider = new LightSensorProvider();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLightSensorProvider.startSensor();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mLightSensorProvider.stopSensor();
    }
}
