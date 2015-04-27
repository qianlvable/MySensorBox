package com.lvable.mysensorbox;

import android.app.AlertDialog;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;


public class AccelerometerActivity extends ActionBarActivity implements SensorEventListener
        ,SurfaceViewInfoBtnClickListener{
    private SensorManager mSensorManager;
    private Sensor mAccelSensor;
    private float mAccelX = 0;
    private float mAccelY = 0;
    private float mAccelZ = 0;
    private long lastUpdateTime;
    private BallSurfaceView renderView;
    private Toolbar mToolbar;
    private AccelerateChangeListener mAccelerateChangeListener;
    private AlertDialog mDialog;
    private FrameLayout layout;
    private TextView mValTextView;
    private LineChart mDataChart;
    private View infoPanelView;
    private final int toolbarColor = 0xff4CAF50;
    private int offset = 100;
    private ArrayList<Entry> mXValData;
    private ArrayList<Entry> mYValData;
    private ArrayList<Entry> mZValData;
    private ArrayList<LineDataSet> mDataSet;

    private boolean isOpen = false;
    private int count = 0;
    ArrayList<String> xVals;

    float outOffset = 0;


    public interface AccelerateChangeListener{
        void dataChange(boolean isPanelOpen,float dx,float dy,float dz);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer);

        setUpToolbar();
        addRenderView();
        addInfoPanel();
        mValTextView = (TextView)findViewById(R.id.tv_val);

        setUpChart();

        ViewTreeObserver vsb = layout.getViewTreeObserver();
        vsb.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                infoPanelView.setTranslationY(layout.getHeight());
                int panelHeight = infoPanelView.getHeight();
                int textViewHeight = infoPanelView.findViewById(R.id.tv_val).getHeight();
                //offset = (int) (layout.getHeight() * 0.9f);
                offset = layout.getHeight() - textViewHeight;
                offset -= textViewHeight /2;

                outOffset = layout.getHeight() - panelHeight;
                ViewTreeObserver obs = layout.getViewTreeObserver();
                Log.d("offset","panelHeight "+ panelHeight+" textviewHeight "+
                        textViewHeight+" offset "+offset+" outOffset "+outOffset);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    obs.removeOnGlobalLayoutListener(this);
                } else {
                    obs.removeGlobalOnLayoutListener(this);
                }
                infoPanelView.setVisibility(View.VISIBLE);
                infoPanelView.animate().translationY(offset).setDuration(500)
                        .setInterpolator(new OvershootInterpolator()).start();
            }
        });


        setUpPanelListener(infoPanelView);

        mSensorManager = MyApplication.getInstance().getSensorManager();
        mAccelSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mAccelerateChangeListener = renderView.getListener();
        mDialog = OtherUtils.getInfoDialog(this, mAccelSensor,getString(R.string.accel_extra_content));


    }

    private void addInfoPanel() {
        infoPanelView = getLayoutInflater().inflate(R.layout.info_panel, null);
        FrameLayout.LayoutParams p = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        infoPanelView.setVisibility(View.INVISIBLE);
        layout.addView(infoPanelView, p);
    }

    private void addRenderView() {
        renderView = new BallSurfaceView(this,this);
        layout = (FrameLayout)findViewById(R.id.accel_layout);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                ,ViewGroup.LayoutParams.MATCH_PARENT);
        layout.addView(renderView, params);
    }

    private void setUpChart() {
        mDataChart = (LineChart)infoPanelView.findViewById(R.id.accel_chart);
        mDataChart.setTouchEnabled(false);
        mDataChart.setMaxVisibleValueCount(10);
        YAxis yl = mDataChart.getAxisLeft();
        yl.setAxisMinValue(-10);
        yl.setAxisMaxValue(10);
        yl.setStartAtZero(false);
        mXValData = new ArrayList<Entry>();
        mYValData = new ArrayList<Entry>();
        mZValData = new ArrayList<Entry>();
        xVals = new ArrayList<String>();
        for (int i =0;i <30;i++){
            mXValData.add(new Entry(0,i));
            mYValData.add(new Entry(0,i));
            mZValData.add(new Entry(0,i));
            xVals.add(i+"");
        }
        LineDataSet xDataSet = new LineDataSet(mXValData,"X");
        LineDataSet yDataSet = new LineDataSet(mYValData,"Y");
        LineDataSet zDataSet = new LineDataSet(mZValData,"Z");
        xDataSet.setColor(getResources().getColor(R.color.light_purple));
        xDataSet.setCircleColor(getResources().getColor(R.color.light_purple));
        xDataSet.setCircleSize(3f);
        yDataSet.setCircleSize(3f);
        zDataSet.setCircleSize(3f);
        yDataSet.setColor(getResources().getColor(R.color.light_orange));
        yDataSet.setCircleColor(getResources().getColor(R.color.light_orange));
        zDataSet.setColor(getResources().getColor(R.color.light_blue));
        zDataSet.setCircleColor(getResources().getColor(R.color.light_orange));

        mDataSet = new ArrayList<LineDataSet>();
        mDataSet.add(xDataSet);
        mDataSet.add(yDataSet);
        mDataSet.add(zDataSet);

        LineData data = new LineData(xVals, mDataSet);
        mDataChart.setData(data);
    }

    private void setUpPanelListener(final View infoPanelView) {
        infoPanelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isOpen) {
                    infoPanelView.animate().translationY(outOffset).setInterpolator(new AccelerateDecelerateInterpolator())
                            .setDuration(350).start();
                    isOpen = true;
                }else {
                    infoPanelView.animate().translationY(offset).setInterpolator(new AccelerateDecelerateInterpolator())
                            .setDuration(350).start();
                    isOpen = false;
                }
            }
        });
    }

    private void setUpToolbar() {
        mToolbar = (Toolbar)findViewById(R.id.toolbar_accel);
        mToolbar.setLogo(R.drawable.logo);
        mToolbar.setTitle(getString(R.string.toolbar_accel_title));
        mToolbar.setBackgroundColor(toolbarColor);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }


    @Override
    protected void onResume() {
        super.onResume();
        renderView.resume();
        mSensorManager.registerListener(this,mAccelSensor,SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        renderView.pause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        long curTime = System.currentTimeMillis();
        if ((curTime - lastUpdateTime)>200) {
            mAccelX = sensorEvent.values[0];
            mAccelY = sensorEvent.values[1];
            mAccelZ = sensorEvent.values[2];
            lastUpdateTime = curTime;
            if (Math.abs(mAccelX) < 0.25 && Math.abs(mAccelY) < 0.25)
                return;
            mAccelerateChangeListener.dataChange(isOpen,mAccelX, mAccelY, mAccelZ);
            mValTextView.setText(String.format("X : %.1f  Y : %.1f  Z : %.1f", mAccelX, mAccelY, mAccelZ));

            if (isOpen)
                updateDataChart();
        }
    }

    private void updateDataChart() {
        Entry xEntry;
        Entry yEntry;
        Entry zEntry;
        if (count < 30){
            xEntry = mXValData.get(count);
            xEntry.setVal(mAccelX);
            yEntry = mYValData.get(count);
            yEntry.setVal(mAccelY);
            zEntry = mZValData.get(count);
            zEntry.setVal(mAccelZ);
            LineData data = new LineData(xVals, mDataSet);
            mDataChart.setData(data);
            mDataChart.invalidate();
            count++;
        }else {
            count = 0;
        }
    }


    @Override
    public void onInfoBtnClick() {
        mDialog.show();
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
