package com.lvable.mysensorbox;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lvable.mysensorbox.audio.process.AudioRecordingHandler;
import com.lvable.mysensorbox.audio.process.AudioRecordingThread;
import com.lvable.mysensorbox.audio.process.BarGraphRenderer;
import com.lvable.mysensorbox.audio.process.VisualizerView;

import java.io.File;


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

        findView();
        setupVisualizer();
        setupToolbar();
        startRecording();
        setupInfoDialog();
    }

    private void findView() {
        visualizerView = (VisualizerView) findViewById(R.id.visualizerView);
        mToolbar = (Toolbar)findViewById(R.id.toolbar_sound);
        dbTextView = (TextView)findViewById(R.id.db_text);
        mInfoBtn = (ImageButton)findViewById(R.id.info_btn_sound);
    }

    private void setupInfoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton(getString(R.string.got_it), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        mDialog = builder.create();
        View dialogLayout = getLayoutInflater().inflate(R.layout.sound_info_dialog,null);
        mDialog.setView(dialogLayout);
        mInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.show();
            }
        });
    }

    private void setupToolbar() {
        mToolbar.setLogo(R.drawable.logo);
        mToolbar.setTitle(getString(R.string.toolbar_sound_title));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
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
        fileName = getFileName();
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

            File f = new File(fileName);
            if(f.exists()) {
                f.delete();
            }
        }
    }


}
