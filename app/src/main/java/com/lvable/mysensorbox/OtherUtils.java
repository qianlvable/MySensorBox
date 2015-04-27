package com.lvable.mysensorbox;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Jiaqi Ning on 23/4/2015.
 */
public class OtherUtils {
    public static AlertDialog getInfoDialog(Activity activity,Sensor sensor,String content){
        View dialogLayout = activity.getLayoutInflater().inflate(R.layout.dialog_info,null);
        TextView sensorNameTV = (TextView)dialogLayout.findViewById(R.id.sensor_name);
        TextView sensorVendorTV = (TextView)dialogLayout.findViewById(R.id.sensor_vendor);
        TextView sensorPowerTV = (TextView)dialogLayout.findViewById(R.id.sensor_power);
        TextView sensorRangeTV = (TextView)dialogLayout.findViewById(R.id.sensor_range);
        TextView contentTV = (TextView)dialogLayout.findViewById(R.id.extra_content);
        sensorNameTV.setText(sensor.getName());
        sensorVendorTV.setText(sensor.getVendor());
        sensorPowerTV.setText(sensor.getPower()+" am");
        sensorRangeTV.setText(sensor.getMaximumRange() + "");
        contentTV.setText(content);
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setPositiveButton(activity.getString(R.string.got_it), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.setView(dialogLayout);

        return dialog;
    }

    public static void saveFirstTimeInfo(SharedPreferences sharedPreferences,String key) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, false);
        editor.apply();
    }
    public static void dismissTutorialView(View behindLayout,View tutorialView) {
        tutorialView.setVisibility(View.GONE);
        behindLayout.setVisibility(View.VISIBLE);
    }



}
