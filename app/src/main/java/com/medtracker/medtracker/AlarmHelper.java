package com.medtracker.medtracker;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by boggs on 11/23/15.
 */
public class AlarmHelper {
    public static final String TAG = "AlarmHelper";

    public static void setAllAlarms(Context context) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        ArrayList<DateObject> dateObjects = databaseHelper.getDates();
        AlarmHelper.setAlarms(context, dateObjects);
    }

    public static void setAlarms(Context context, ArrayList<DateObject> dateObjects) {
        for(DateObject dateObject : dateObjects) {
            AlarmHelper.setAlarm(context, dateObject);
        }
    }

    public static void setAlarm(Context context, DateObject dateObject) {
        Intent intent = new Intent(context, AlarmReceiver.class);

        long millis = dateObject.getMillis();

        int id = PendingIntentHelper.getNewPendingIntentId(context);
        int requestCode = id;
        int dateId = dateObject.getId();
        int medicineId = dateObject.getMedicineId();

        MedicineObject medicineObject = MedicineHelper.getMedicine(context, medicineId);
        DateFormat dateFormat = DateFormat.getTimeInstance(DateFormat.SHORT);

        Date time = new Date();
        time.setTime(millis);

        intent.putExtra("medicineName", medicineObject.getName());
        intent.putExtra("time", String.valueOf(dateFormat.format(time)));
        intent.putExtra("dosageAmount", String.valueOf(medicineObject.getDosageAmount()));
        intent.putExtra("dosageUnit", String.valueOf(medicineObject.getDosageUnit()));
        intent.putExtra("requestCode", requestCode);

        PendingIntentObject pendingIntentObject = new PendingIntentObject(id, requestCode, dateId, medicineId);
        PendingIntentHelper.addPendingIntent(context, pendingIntentObject);

        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntentHelper.getAlarmManager(context).set(AlarmManager.RTC_WAKEUP, millis, alarmIntent);

        Log.d(TAG, String.format("%s %s", "setAlarm", pendingIntentObject.toString()));
    }

    public static void cancelAlarmFromMedicineId(Context context, int medicineId) {
        PendingIntentHelper.cancelPendingIntentsFromMedicineId(context, medicineId);
    }
}
