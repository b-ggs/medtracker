package com.medtracker.medtracker;

import android.content.Context;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.util.Log;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by boggs on 11/22/15.
 */
public class DateHelper {
    public static final String TAG = "DateHelper";

    public static void generateDates(Context context, MedicineObject medicineObject) {
        ArrayList<Calendar> finalDateTimes = new ArrayList<>();

        int numberDaysTake = medicineObject.getNumberDaysTake();
        ArrayList<String> daysToTake = medicineObject.getDaysToTake();
        Calendar startDate = medicineObject.getStartDate();
        ArrayList<Calendar> times = medicineObject.getTimes();

        ArrayList<Date> datesRaw = DateHelper.getDates(numberDaysTake, daysToTake, startDate);

        DateFormat dateFormat = DateFormat.getDateTimeInstance();

        for(Date date : datesRaw) {
            Calendar dateCalendar = Calendar.getInstance();
            dateCalendar.setTime(date);
            for(Calendar timeCalendar : times) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.MONTH, dateCalendar.get(Calendar.MONTH));
                calendar.set(Calendar.DAY_OF_MONTH, dateCalendar.get(Calendar.DAY_OF_MONTH));
                calendar.set(Calendar.YEAR, dateCalendar.get(Calendar.YEAR));
                calendar.set(Calendar.HOUR_OF_DAY, timeCalendar.get(Calendar.HOUR_OF_DAY));
                calendar.set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE));
                calendar.set(Calendar.SECOND, 0);

                Log.d(TAG, String.format("generateDates dateCalendar + timeCalendar %s", dateFormat.format(calendar.getTime())));
                finalDateTimes.add(calendar);
            }
        }

        for(Calendar finalDateTime : finalDateTimes) {
            Log.d(TAG, String.format("generateDates finalDateTime %s", dateFormat.format(finalDateTime.getTime())));
            DateHelper.addDate(context, finalDateTime.getTimeInMillis(), medicineObject.getId());
        }
    }

    public static ArrayList<Date> getDates(int numberDaysTake, ArrayList<String> daysToTake, Calendar startDate) {
        ArrayList<Date> dates = new ArrayList<>();
        ArrayList<Integer> daysToTakeConstant = DateHelper.formatDaysToTakeToConstant(daysToTake);

        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.setTimeInMillis(startDate.getTimeInMillis());

        DateFormat dateFormat = DateFormat.getDateInstance();

        int counter = 1;
        while(counter <= numberDaysTake) {
            if(daysToTakeConstant.contains(currentCalendar.get(Calendar.DAY_OF_WEEK))) {
                dates.add(currentCalendar.getTime());
                counter++;
            }
            currentCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        return dates;
    }

    public static ArrayList<Integer> formatDaysToTakeToConstant(ArrayList<String> daysToTake) {
        ArrayList<Integer> daysToTakeSorted = new ArrayList<>();
        String[] days = {"Su", "Mo", "Tu", "We", "Th", "Fr", "Sa"};
        Integer[] daysConstant = {
                Calendar.SUNDAY,
                Calendar.MONDAY,
                Calendar.TUESDAY,
                Calendar.WEDNESDAY,
                Calendar.THURSDAY,
                Calendar.FRIDAY,
                Calendar.SATURDAY
        };

        for(int i = 0; i < days.length; i++) {
            if(daysToTake.contains(days[i])) {
                daysToTakeSorted.add(daysConstant[i]);
            }
        }

        return daysToTakeSorted;
    }

    public static int getNewId(Context context) {
        int max = 0;
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        for(DateObject dateObject: databaseHelper.getDates()) {
            if(dateObject.getId() > max)
                max = dateObject.getId();
        }
        return max + 1;
    }

    public static void addDate(Context context, long millis, int medicineId) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        databaseHelper.addDate(new DateObject(DateHelper.getNewId(context), millis, medicineId));
    }

    public static void removeDatesFromMedicineId(Context context, int medicineId) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        databaseHelper.removeDatesFromMedicineId(medicineId);
    }

    public static ArrayList<Date> getDatesForCaldroid(Context context) {
        ArrayList<Date> datesList = new ArrayList<>();
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        ArrayList<DateObject> dateObjects = databaseHelper.getDates();

        for(DateObject dateObject : dateObjects) {
//            datesList.add(new Date(dateObject.getMillis()));
            DateFormat dateFormat = DateFormat.getDateTimeInstance();
            Log.d(TAG, String.format("getDatesForCaldroid %s", dateFormat.format(dateObject.getDateOnly())));
            datesList.add(dateObject.getDateOnly());
        }

        return datesList;
    }

    public static ArrayList<DateObject> getDatesFromDateOnly(Context context, Date date) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        return databaseHelper.getDatesFromDateOnly(date);
    }

    public static ArrayList<DateObject> getDatesFromMedicineId(Context context, int medicineId) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        return databaseHelper.getDatesFromMedicineId(medicineId);
    }
}
