package com.lvable.mysensorbox;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lvable.mysensorbox.audio.process.AudioRecordingHandler;
import com.lvable.mysensorbox.audio.process.AudioRecordingThread;
import com.lvable.mysensorbox.audio.process.BarGraphRenderer;
import com.lvable.mysensorbox.audio.process.VisualizerView;
import com.lvable.mysensorbox.sensor.framework.util.SoundSensorProvider;


public class SoundWaveActivity extends ActionBarActivity {
    private static final int DIVISIONS = 8;
    private VisualizerView visualizerView;
    private AudioRecordingThread recordingThread;
    private static final String AUDIO_FILE_NAME = "record_test.raw";
    private String fileName;
    private TextView dbTextView;
    private Toolbar mToolbar;
    private ImageButton mInfoBtn;
    private AlertDialog mDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_wave);
        visualizerView = (VisualizerView) findViewById(R.id.visualizerView);
        mToolbar = (Toolbar)findViewById(R.id.toolbar_sound);
        mToolbar.setLogo(R.drawable.logo);
        mToolbar.setTitle("Sound wave sensor");
        dbTextView = (TextView)findViewById(R.id.db_text);
        mInfoBtn = (ImageButton)findViewById(R.id.info_btn_sound);
        setupVisualizer();
        fileName = getFileName();
        startRecording();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("Got it!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        mDialog = builder.create();
        mDialog.setTitle("Behind the scene");
        mDialog.setMessage("FFT algorithm");

        mInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.show();
            }
        });
    }

    public static String getFileName() {
        String storageDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        return String.format("%s/%s", storageDir,AUDIO_FILE_NAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopRecording();
    }

    private void setupVisualizer() {
        Paint paint = new Paint();
        paint.setStrokeWidth(25f);                     //set bar width
        paint.setAntiAlias(true);
        paint.setColor(Color.argb(200, 255, 255, 255)); //set bar color
        BarGraphRenderer barGraphRendererBottom = new BarGraphRenderer(DIVISIONS, paint, false);

        visualizerView.addRenderer(barGraphRendererBottom);
    }

    private void startRecording() {
        recordingThread = new AudioRecordingThread(fileName, new AudioRecordingHandler() { //pass file name where to store the recorded audio
            @Override
            public void onFftDataCapture(final byte[] bytes) {

                runOnUiThread(new Runnable() {
                    public void run() {
                        if (visualizerView != null) {
                            visualizerView.updateVisualizerFFT(bytes); //update VisualizerView with new audio portion
                        }
                    }
                });
            }
            @Override
            public void onAmplitudeChange(final double amp) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String ampStr = String.format("%.2f", amp) + " db";
                        dbTextView.setText(ampStr);
                    }
                });

            }

            @Override
            public void onRecordSuccess() {}

            @Override
            public void onRecordingError() {}

            @Override
            public void onRecordSaveError() {}


        });
        recordingThread.start();
    }

    private void stopRecording() {
        if (recordingThread != null) {
            recordingThread.stopRecording();
            recordingThread = null;
        }
    }


}
