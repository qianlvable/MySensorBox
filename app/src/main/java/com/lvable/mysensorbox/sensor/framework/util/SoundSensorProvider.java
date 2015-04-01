package com.lvable.mysensorbox.sensor.framework.util;

import android.media.MediaRecorder;

import java.io.IOException;

/**
 * Created by Jiaqi Ning on 27/3/2015.
 */
public class SoundSensorProvider {
    private MediaRecorder mRecorder = null;

    public void startSensor() {
        if (mRecorder == null) {
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.setAudioSamplingRate(16000);
            mRecorder.setOutputFile("/dev/null");
            try {
                mRecorder.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mRecorder.start();
        }
    }

    public void stopSensor() {
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }
    }

    public double getAmplitude() {
        if (mRecorder != null)
            return  mRecorder.getMaxAmplitude();
        else
            return 0;

    }

}
