package com.medtracker.medtracker;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by boggs on 11/23/15.
 */
public class PendingIntentHelper {
    public static final String TAG = "PendingIntentHelper";

    public static AlarmManager getAlarmManager(Context context) {
        return (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    public static PendingIntent getPendingIntent(Context context, int requestCode) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        return PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static void addPendingIntent(Context context, PendingIntentObject pendingIntentObject) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        databaseHelper.addPendingIntent(pendingIntentObject);
        Log.d(TAG, String.format("%s %s", "addPendingIntent", pendingIntentObject.toString()));
    }

    public static void cancelPendingIntentsFromMedicineId(Context context, int medicineId) {
        for(PendingIntentObject pendingIntentObject : getPendingIntentsFromMedicineId(context, medicineId)) {
            getAlarmManager(context).cancel(getPendingIntent(context, pendingIntentObject.getRequestCode()));
            Log.d(TAG, String.format("%s %s", "cancelPendingIntentsFromMedicineId", pendingIntentObject.toString()));
        }
        removePendingIntentFromMedicineId(context, medicineId);
    }

    public static ArrayList<PendingIntentObject> getPendingIntents(Context context) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        return databaseHelper.getPendingIntents();
    }

    public static ArrayList<PendingIntentObject> getPendingIntentsFromMedicineId(Context context, int medicineId) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        return databaseHelper.getPendingIntentsFromMedicineId(medicineId);
    }

    public static void removePendingIntentFromMedicineId(Context context, int medicineId) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        databaseHelper.removePendingIntentsFromMedicineId(medicineId);
    }

    public static int getNewPendingIntentId(Context context) {
        int max = 0;
        for(PendingIntentObject pendingIntentObject : getPendingIntents(context)) {
            if(pendingIntentObject.getId() > max) {
                max = pendingIntentObject.getId();
            }
        }
        return max + 1;
    }
}
