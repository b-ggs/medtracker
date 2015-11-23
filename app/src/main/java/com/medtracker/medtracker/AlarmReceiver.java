package com.medtracker.medtracker;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by boggs on 11/23/15.
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        if(extras != null) {
            String medicineName = extras.getString("medicineName");
            String time = extras.getString("time");
            String dosageAmount = extras.getString("dosageAmount");
            String dosageUnit = extras.getString("dosageUnit");
            int requestCode = extras.getInt("requestCode");
            executeNotification(context, medicineName, time, dosageAmount, dosageUnit, requestCode);
        } else {
            Toast.makeText(context, "null extras", Toast.LENGTH_SHORT).show();
        }
    }

    public void executeNotification(Context context, String medicineName, String time, String dosageAmount, String dosageUnit, int requestCode) {
        Intent resultIntent = new Intent(context, CalendarViewActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(String.format("Time to take %s!", medicineName))
                .setContentText(String.format("It's %s! Take %s %s of %s!", time, dosageAmount, dosageUnit, medicineName))
                .setSound(alarmSound)
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .setContentIntent(resultPendingIntent);

        Notification notification = notificationBuilder.build();
        notification.defaults = Notification.DEFAULT_ALL;

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(requestCode, notification);
    }
}
