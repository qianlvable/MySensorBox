package com.lvable.mysensorbox;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.lvable.mysensorbox.sensor.framework.util.ProximitySensorProvider;


public class DistanceActivity extends ActionBarActivity {
    private ProximitySensorProvider sensor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distance);

        sensor = new ProximitySensorProvider();
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensor.startSensor();
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensor.stopSensor();
    }
}
