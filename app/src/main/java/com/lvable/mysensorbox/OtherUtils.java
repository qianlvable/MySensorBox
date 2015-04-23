package com.lvable.mysensorbox;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Jiaqi Ning on 23/4/2015.
 */
public class OtherUtils {
    public static AlertDialog getInfoDialog(Activity activity,Sensor sensor){
        View dialogLayout = activity.getLayoutInflater().inflate(R.layout.dialog_info,null);
        TextView sensorNameTV = (TextView)dialogLayout.findViewById(R.id.sensor_name);
        TextView sensorVendorTV = (TextView)dialogLayout.findViewById(R.id.sensor_vendor);
        TextView sensorPowerTV = (TextView)dialogLayout.findViewById(R.id.sensor_power);
        TextView sensorRangeTV = (TextView)dialogLayout.findViewById(R.id.sensor_range);
        sensorNameTV.setText(sensor.getName());
        sensorVendorTV.setText(sensor.getVendor());
        sensorPowerTV.setText(sensor.getPower()+" am");
        sensorRangeTV.setText(sensor.getMaximumRange() + "");
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setPositiveButton("Got it!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.setView(dialogLayout);

        return dialog;
    }
}
