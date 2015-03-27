package com.lvable.mysensorbox;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.lvable.mysensorbox.sensor.framework.util.CompassProvider;


public class CompassActivity extends ActionBarActivity {
    CompassProvider mCompassProvider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);

        mCompassProvider = new CompassProvider();

    }

    @Override
    protected void onResume() {
        super.onResume();
        mCompassProvider.startSensor();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCompassProvider.stopSensor();
    }
}
