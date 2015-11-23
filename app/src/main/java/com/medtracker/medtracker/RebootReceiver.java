package com.medtracker.medtracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by boggs on 11/23/15.
 */
public class RebootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "MedTracker has restarted.", Toast.LENGTH_SHORT).show();
        AlarmHelper.setAllAlarms(context);
    }
}
