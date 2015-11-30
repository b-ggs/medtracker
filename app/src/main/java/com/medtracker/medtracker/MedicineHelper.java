package com.medtracker.medtracker;

import android.content.Context;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by boggs on 11/22/15.
 */
public class MedicineHelper {
    public static String formatTimesForDatabase(MedicineObject medicineObject) {
        ArrayList<Calendar> times = medicineObject.getTimes();
        StringBuilder stringBuilder = new StringBuilder();
        for(Calendar calendar : times) {
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            stringBuilder.append(String.format("%s:%s ", hour, minute));
        }

        return stringBuilder.toString().trim();
    }

    public static ArrayList<Calendar> formatTimesForObject(String string) {
        ArrayList<Calendar> times = new ArrayList<>();
        for(String s : string.split(" ")) {
            int hour = Integer.valueOf(s.split(":")[0]);
            int minute = Integer.valueOf(s.split(":")[1]);

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            times.add(calendar);
        }
        return times;
    }

    public static String formatTimesForAdapter(MedicineObject medicineObject) {
        ArrayList<Calendar> timeObjects = MedicineHelper.formatTimesForObject(MedicineHelper.formatTimesForDatabase(medicineObject));
        StringBuilder stringBuilder = new StringBuilder();
        for(Calendar timeObject : timeObjects) {
            DateFormat dateFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
            stringBuilder.append(String.format("%s ", dateFormat.format(timeObject.getTime())));
        }
        return stringBuilder.toString().trim();
    }

    public static String formatStartDateForDatabase(MedicineObject medicineObject) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(medicineObject.getStartDate());
        return String.valueOf(calendar.getTimeInMillis());
    }

    public static String formatEndDateForDatabase(MedicineObject medicineObject) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(medicineObject.getEndDate());
        return String.valueOf(calendar.getTimeInMillis());
    }

    public static String formatStartTimeForDatabase(MedicineObject medicineObject) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(medicineObject.getStartTime());
        return String.valueOf(calendar.getTimeInMillis());
    }

    public static Date formatStartDateForObject(String string) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.valueOf(string));
        return calendar.getTime();
    }

    public static Date formatEndDateForObject(String string) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.valueOf(string));
        return calendar.getTime();
    }

    public static String formatDaysToTakeForDatabase(MedicineObject medicineObject) {
        ArrayList<String> days = medicineObject.getDaysToTake();
        StringBuilder stringBuilder = new StringBuilder();
        for(String day : days) {
            stringBuilder.append(String.format("%s ", day));
        }
        return stringBuilder.toString().trim();
    }

    public static ArrayList<String> formatDaysToTakeForObject(String string) {
        ArrayList<String> days = new ArrayList<>();
        for(String s : string.split(" ")) {
            days.add(s);
        }
        return days;
    }

    public static String getAllMedicinesToString(Context context) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        StringBuilder stringBuilder = new StringBuilder();
        for(MedicineObject medicineObject : databaseHelper.getMedicines()) {
            stringBuilder.append(medicineObject.toString());
        }
        return stringBuilder.toString();
    }

    public static ArrayList<String> sortDaysToTake(ArrayList<String> daysToTake) {
        ArrayList<String> daysToTakeSorted = new ArrayList<>();
        String[] days = {"Su", "Mo", "Tu", "We", "Th", "Fr", "Sa"};

        for(String day : days) {
            if(daysToTake.contains(day)) {
                daysToTakeSorted.add(day);
            }
        }

        return daysToTakeSorted;
    }

    public static void addMedicine(Context context, MedicineObject medicineObject) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        databaseHelper.addMedicine(medicineObject);
        DateHelper.generateDates(context, medicineObject);
        AlarmHelper.setAlarms(context, DateHelper.getDatesFromMedicineId(context, medicineObject.getId()));
    }

    public static MedicineObject getMedicine(Context context, int id) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        return databaseHelper.getMedicine(id);
    }

    public static int getNewId(Context context) {
        int max = 0;
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        for(MedicineObject medicineObject : databaseHelper.getMedicines()) {
            if(medicineObject.getId() > max)
                max = medicineObject.getId();
        }
        return max + 1;
    }

    public static ArrayList<MedicineObject> getAllMedicines(Context context) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        return databaseHelper.getMedicines();
    }

    public static void removeMedicine(Context context, int id) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        databaseHelper.removeMedicine(id);
        DateHelper.removeDatesFromMedicineId(context, id);
        AlarmHelper.cancelAlarmFromMedicineId(context, id);
    }

    public static ArrayList<MedicineObject> getAllMedicineFromDateObjects(Context context, ArrayList<DateObject> dateObjects) {
        ArrayList<MedicineObject> medicineObjects = new ArrayList<>();
        ArrayList<Integer> medicineIds = new ArrayList<>();
        for(DateObject dateObject : dateObjects) {
            MedicineObject medicineObject = MedicineHelper.getMedicine(context, dateObject.getMedicineId());
            if(!medicineIds.contains(medicineObject.getId())) {
                medicineObjects.add(medicineObject);
                medicineIds.add(medicineObject.getId());
            }
        }
        return medicineObjects;
    }
}
