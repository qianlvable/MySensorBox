package com.lvable.mysensorbox;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ValueFormatter;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class MagneticActivity extends ActionBarActivity implements SensorEventListener{
    private SensorManager mSensorManager;
    private Sensor mMagSensor;
    private Toolbar mToolbar;
    private ArrayList<BarEntry> mData;
    private ArrayList<BarDataSet> mDataSets;
    private BarDataSet mSet;
    private float magnitude = 0;
    private HorizontalBarChart mBarChart;
    private ArrayList<String> xVals;
    private int mMaxRange = 150;
    private TextView mMagValTextView;
    private MyValueFormatter mValueFormatter;
     @Override
    protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_magnetic);

         mSensorManager = MyApplication.getInstance().getSensorManager();
         mMagSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

         mMagValTextView = (TextView)findViewById(R.id.tv_mag_val);
         mToolbar = (Toolbar)findViewById(R.id.toolbar_magnetic);
         mToolbar.setLogo(R.drawable.logo);
         mToolbar.setTitle("Magnetic Field");

         setupCharUI();
     }

    private void setupCharUI() {
        mDataSets = new ArrayList<BarDataSet>();
        mData = new ArrayList<BarEntry>();
        mData.add(new BarEntry(0, 0));
        mData.add(new BarEntry(0,1));
        mData.add(new BarEntry(0, 2));
        mSet = new BarDataSet(mData,"μT");
        mSet.setBarSpacePercent(35f);
        mDataSets.add(mSet);

        mBarChart = (HorizontalBarChart)findViewById(R.id.mag_chart);
        mBarChart.setDrawValueAboveBar(true);
        mBarChart.setTouchEnabled(false);
        mBarChart.setMaxVisibleValueCount(mMaxRange);
        mBarChart.setDrawGridBackground(false);
        mBarChart.setDescription("");


        YAxis yl = mBarChart.getAxisLeft();
        yl.setDrawAxisLine(true);
        yl.setTextColor(Color.WHITE);
        yl.setStartAtZero(false);
        yl.setAxisMaxValue(mMaxRange);
        yl.setAxisMinValue(-mMaxRange);
        mValueFormatter = new MyValueFormatter();
        yl.setValueFormatter(mValueFormatter);


        YAxis rl = mBarChart.getAxisRight();
        rl.setEnabled(false);

        XAxis xl = mBarChart.getXAxis();
        xl.setTextColor(Color.WHITE);
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);

        xVals = new ArrayList<String>();
        xVals.add("Z");
        xVals.add("Y");
        xVals.add("X");
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this,mMagSensor,SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this,mMagSensor);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float x = sensorEvent.values[0];
        float y = sensorEvent.values[1];
        float z = sensorEvent.values[2];
        Log.d("magnetic",x+","+y+","+z);
        magnitude = (float) Math.sqrt(x*x + y*y + z*z);
        mMagValTextView.setText(String.format("%.1f μT", magnitude));

        mData.get(0).setVal(x);
        mData.get(1).setVal(y);
        mData.get(2).setVal(z);

        BarData data = new BarData(xVals,mDataSets);
        data.setValueFormatter(mValueFormatter);
        mBarChart.setData(data);


        mBarChart.invalidate();

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_magnetic, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

    private class MyValueFormatter implements ValueFormatter {

        private DecimalFormat mFormat;

        public MyValueFormatter() {
            mFormat = new DecimalFormat("###,###,##0.0"); // use one decimal
        }

        @Override
        public String getFormattedValue(float value) {
            return mFormat.format(value);
        }
    }
}
