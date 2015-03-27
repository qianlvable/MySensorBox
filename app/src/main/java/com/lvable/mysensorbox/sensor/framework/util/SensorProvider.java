package com.lvable.mysensorbox.sensor.framework.util;

import android.hardware.SensorEventListener;

/**
 * Created by Jiaqi Ning on 27/3/2015.
 */
public abstract class SensorProvider implements SensorEventListener {
    public abstract void startSensor();
    public abstract void stopSensor();
    public abstract float getMaxRange();
}
