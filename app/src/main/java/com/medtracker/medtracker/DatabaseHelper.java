package com.medtracker.medtracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by boggs on 11/22/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String TAG = "DatabaseHelper";

    public static final int DATABASE_VERSION = 4;
    public static final String DATABASE_NAME = "MedTracker.db";

    public static final String MEDICINE_TABLE = "medicine";
    public static final String MEDICINE_COLUMN_ID = "id";
    public static final String MEDICINE_COLUMN_NAME = "name";
    public static final String MEDICINE_COLUMN_DOSAGE_AMOUNT = "dosageAmount";
    public static final String MEDICINE_COLUMN_DOSAGE_UNIT = "dosageUnit";
    public static final String MEDICINE_COLUMN_NUMBER_DAYS_TAKE = "numberDaysToTake";
    public static final String MEDICINE_COLUMN_DAYS_TO_TAKE = "daysToTake";
    public static final String MEDICINE_COLUMN_TIMES = "times";
    public static final String MEDICINE_COLUMN_START_DATE = "starting";
    public static final String MEDICINE_COLUMN_NOTES = "notes";

    public static final String DATE_TABLE = "date";
    public static final String DATE_COLUMN_ID = "id";
    public static final String DATE_COLUMN_MILLIS = "dateMillis";
    public static final String DATE_COLUMN_DATE_ONLY = "dateOnly";
    public static final String DATE_COLUMN_MEDICINE_ID= "medicineId";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, String.format("Database create version %s.", DATABASE_VERSION));
        initializeMedicineTable(db);
        initializeDateTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, String.format("Database upgrade from %s to %s.", oldVersion, newVersion));
        dropMedicineTable(db);
        dropDateTable(db);
        onCreate(db);
    }

    public void initializeMedicineTable(SQLiteDatabase db) {
        StringBuilder sql = new StringBuilder();
        sql.append(String.format("CREATE TABLE %s ", MEDICINE_TABLE));
        sql.append("(");
        sql.append(String.format("%s %s, ", MEDICINE_COLUMN_ID, "INTEGER PRIMARY KEY"));
        sql.append(String.format("%s %s, ", MEDICINE_COLUMN_NAME, "TEXT"));
        sql.append(String.format("%s %s, ", MEDICINE_COLUMN_DOSAGE_AMOUNT, "TEXT"));
        sql.append(String.format("%s %s, ", MEDICINE_COLUMN_DOSAGE_UNIT, "TEXT"));
        sql.append(String.format("%s %s, ", MEDICINE_COLUMN_NUMBER_DAYS_TAKE, "TEXT"));
        sql.append(String.format("%s %s, ", MEDICINE_COLUMN_TIMES, "TEXT"));
        sql.append(String.format("%s %s, ", MEDICINE_COLUMN_DAYS_TO_TAKE, "TEXT"));
        sql.append(String.format("%s %s, ", MEDICINE_COLUMN_START_DATE, "TEXT"));
        sql.append(String.format("%s %s ", MEDICINE_COLUMN_NOTES, "TEXT"));
        sql.append(")");
        Log.d(TAG, String.format("Exec: %s", sql.toString()));
        db.execSQL(sql.toString());
    }

    public void initializeDateTable(SQLiteDatabase db) {
        StringBuilder sql = new StringBuilder();
        sql.append(String.format("CREATE TABLE %s ", DATE_TABLE));
        sql.append("(");
        sql.append(String.format("%s %s, ", DATE_COLUMN_ID, "INTEGER PRIMARY KEY"));
        sql.append(String.format("%s %s, ", DATE_COLUMN_MILLIS, "TEXT"));
        sql.append(String.format("%s %s, ", DATE_COLUMN_DATE_ONLY, "TEXT"));
        sql.append(String.format("%s %s ", DATE_COLUMN_MEDICINE_ID, "TEXT"));
        sql.append(")");
        Log.d(TAG, String.format("Exec: %s", sql.toString()));
        db.execSQL(sql.toString());
    }

    public void dropMedicineTable(SQLiteDatabase db) {
        db.execSQL(String.format("DROP TABLE IF EXISTS %s", MEDICINE_TABLE));
    }

    public void dropDateTable(SQLiteDatabase db) {
        db.execSQL(String.format("DROP TABLE IF EXISTS %s", DATE_TABLE));
    }

    public void addMedicine(MedicineObject medicineObject) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        int id = medicineObject.getId();
        String name = medicineObject.getName();
        String dosageAmount = String.valueOf(medicineObject.getDosageAmount());
        String dosageUnit = String.valueOf(medicineObject.getDosageUnit());
        String numberDaysTake = String.valueOf(medicineObject.getNumberDaysTake());
        String times = MedicineHelper.formatTimesForDatabase(medicineObject);
        String daysToTake = MedicineHelper.formatDaysToTakeForDatabase(medicineObject);
        String startDate = MedicineHelper.formatStartDateForDatabase(medicineObject);
        String notes = medicineObject.getNotes();

        values.put(MEDICINE_COLUMN_ID, id);
        values.put(MEDICINE_COLUMN_NAME, name);
        values.put(MEDICINE_COLUMN_DOSAGE_AMOUNT, dosageAmount);
        values.put(MEDICINE_COLUMN_DOSAGE_UNIT, dosageUnit);
        values.put(MEDICINE_COLUMN_TIMES, times);
        values.put(MEDICINE_COLUMN_NUMBER_DAYS_TAKE, numberDaysTake);
        values.put(MEDICINE_COLUMN_DAYS_TO_TAKE, daysToTake);
        values.put(MEDICINE_COLUMN_START_DATE, startDate);
        values.put(MEDICINE_COLUMN_NOTES, notes);

        db.insert(MEDICINE_TABLE, null, values);
        db.close();
    }

    public void addDate(DateObject dateObject) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        int id = dateObject.getId();
        String millis = String.valueOf(dateObject.getMillis());
        String medicineId = String.valueOf(dateObject.getMedicineId());
        String dateOnly = String.valueOf(dateObject.getDate().getTime());

        values.put(DATE_COLUMN_ID, id);
        values.put(DATE_COLUMN_MILLIS, millis);
        values.put(DATE_COLUMN_DATE_ONLY, dateOnly);
        values.put(DATE_COLUMN_MEDICINE_ID, medicineId);

        db.insert(DATE_TABLE, null, values);
        db.close();
    }

    public ArrayList<MedicineObject> getMedicines() {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<MedicineObject> medicineObjects = new ArrayList<>();

        db.rawQuery(String.format("SELECT * FROM %s ", MEDICINE_TABLE), null);

        Cursor cursor = db.query(MEDICINE_TABLE,
                new String[]{
                        MEDICINE_COLUMN_ID,
                        MEDICINE_COLUMN_NAME,
                        MEDICINE_COLUMN_DOSAGE_AMOUNT,
                        MEDICINE_COLUMN_DOSAGE_UNIT,
                        MEDICINE_COLUMN_TIMES,
                        MEDICINE_COLUMN_DAYS_TO_TAKE,
                        MEDICINE_COLUMN_START_DATE,
                        MEDICINE_COLUMN_NOTES,
                        MEDICINE_COLUMN_NUMBER_DAYS_TAKE
                },
                null, // id = ?
                null,
                null,
                null,
                null
        );

        if(cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(MEDICINE_COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndex(MEDICINE_COLUMN_NAME));
                float dosageAmount = Float.valueOf(cursor.getString(cursor.getColumnIndex(MEDICINE_COLUMN_DOSAGE_AMOUNT)));
                String dosageUnit = cursor.getString(cursor.getColumnIndex(MEDICINE_COLUMN_DOSAGE_UNIT));
                int numberDaysTake = Integer.valueOf(cursor.getString(cursor.getColumnIndex(MEDICINE_COLUMN_NUMBER_DAYS_TAKE)));
                ArrayList<Calendar> times = MedicineHelper.formatTimesForObject(cursor.getString(cursor.getColumnIndex(MEDICINE_COLUMN_TIMES)));
                Calendar startDate = MedicineHelper.formatStartDateForObject(cursor.getString(cursor.getColumnIndex(MEDICINE_COLUMN_START_DATE)));
                ArrayList<String> daysToTake = MedicineHelper.formatDaysToTakeForObject(cursor.getString(cursor.getColumnIndex(MEDICINE_COLUMN_DAYS_TO_TAKE)));
                String notes = cursor.getString(cursor.getColumnIndex(MEDICINE_COLUMN_NOTES));

                MedicineObject medicineObject = new MedicineObject(id, name, dosageAmount, dosageUnit, numberDaysTake, daysToTake, times, startDate, notes);
                medicineObjects.add(medicineObject);
            } while(cursor.moveToNext());
        }

        db.close();
        cursor.close();

        return medicineObjects;
    }

    public MedicineObject getMedicine(int idQuery) {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<MedicineObject> medicineObjects = new ArrayList<>();

        db.rawQuery(String.format("SELECT * FROM %s ", MEDICINE_TABLE), null);

        Cursor cursor = db.query(MEDICINE_TABLE,
                new String[]{
                        MEDICINE_COLUMN_ID,
                        MEDICINE_COLUMN_NAME,
                        MEDICINE_COLUMN_DOSAGE_AMOUNT,
                        MEDICINE_COLUMN_DOSAGE_UNIT,
                        MEDICINE_COLUMN_TIMES,
                        MEDICINE_COLUMN_DAYS_TO_TAKE,
                        MEDICINE_COLUMN_START_DATE,
                        MEDICINE_COLUMN_NOTES,
                        MEDICINE_COLUMN_NUMBER_DAYS_TAKE
                },
                String.format("%s=?", MEDICINE_COLUMN_ID), // id = ?
                new String[] { String.valueOf(idQuery) },
                null,
                null,
                null
        );

        if(cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(MEDICINE_COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndex(MEDICINE_COLUMN_NAME));
                float dosageAmount = Float.valueOf(cursor.getString(cursor.getColumnIndex(MEDICINE_COLUMN_DOSAGE_AMOUNT)));
                String dosageUnit = cursor.getString(cursor.getColumnIndex(MEDICINE_COLUMN_DOSAGE_UNIT));
                int numberDaysTake = Integer.valueOf(cursor.getString(cursor.getColumnIndex(MEDICINE_COLUMN_NUMBER_DAYS_TAKE)));
                ArrayList<Calendar> times = MedicineHelper.formatTimesForObject(cursor.getString(cursor.getColumnIndex(MEDICINE_COLUMN_TIMES)));
                Calendar startDate = MedicineHelper.formatStartDateForObject(cursor.getString(cursor.getColumnIndex(MEDICINE_COLUMN_START_DATE)));
                ArrayList<String> daysToTake = MedicineHelper.formatDaysToTakeForObject(cursor.getString(cursor.getColumnIndex(MEDICINE_COLUMN_DAYS_TO_TAKE)));
                String notes = cursor.getString(cursor.getColumnIndex(MEDICINE_COLUMN_NOTES));

                MedicineObject medicineObject = new MedicineObject(id, name, dosageAmount, dosageUnit, numberDaysTake, daysToTake, times, startDate, notes);
                medicineObjects.add(medicineObject);
            } while(cursor.moveToNext());
        }

        db.close();
        cursor.close();

        return medicineObjects.get(0);
    }

    public ArrayList<DateObject> getDates() {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<DateObject> dateObjects = new ArrayList<>();

        db.rawQuery(String.format("SELECT * FROM %s ", DATE_TABLE), null);

        Cursor cursor = db.query(DATE_TABLE,
                new String[]{
                        DATE_COLUMN_ID,
                        DATE_COLUMN_MILLIS,
                        DATE_COLUMN_DATE_ONLY,
                        DATE_COLUMN_MEDICINE_ID
                },
                null, // id = ?
                null,
                null,
                null,
                null
        );

        if(cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(DATE_COLUMN_ID));
                long millis = Long.valueOf(cursor.getString(cursor.getColumnIndex(DATE_COLUMN_MILLIS)));
                int medicineId = Integer.valueOf(cursor.getString(cursor.getColumnIndex(DATE_COLUMN_MEDICINE_ID)));

                DateObject dateObject = new DateObject(id, millis, medicineId);
                dateObjects.add(dateObject);
            } while(cursor.moveToNext());
        }

        db.close();
        cursor.close();

        return dateObjects;
    }

    public ArrayList<DateObject> getDatesFromDateOnly(Date date) {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<DateObject> dateObjects = new ArrayList<>();

        db.rawQuery(String.format("SELECT * FROM %s ", DATE_TABLE), null);

        Cursor cursor = db.query(DATE_TABLE,
                new String[]{
                        DATE_COLUMN_ID,
                        DATE_COLUMN_MILLIS,
                        DATE_COLUMN_DATE_ONLY,
                        DATE_COLUMN_MEDICINE_ID
                },
                String.format("%s=?", DATE_COLUMN_DATE_ONLY), // id = ?
                new String[] { String.valueOf(date.getTime()) },
                null,
                null,
                null
        );

        if(cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(DATE_COLUMN_ID));
                long millis = Long.valueOf(cursor.getString(cursor.getColumnIndex(DATE_COLUMN_MILLIS)));
                int medicineId = Integer.valueOf(cursor.getString(cursor.getColumnIndex(DATE_COLUMN_MEDICINE_ID)));

                DateObject dateObject = new DateObject(id, millis, medicineId);
                dateObjects.add(dateObject);
            } while(cursor.moveToNext());
        }

        db.close();
        cursor.close();

        return dateObjects;
    }

    public void removeMedicine(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(MEDICINE_TABLE, MEDICINE_COLUMN_ID + " =? ", new String[]{String.valueOf(id)});
        db.close();
    }

    public void removeDatesFromMedicineId(int medicineId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(DATE_TABLE, DATE_COLUMN_MEDICINE_ID + " =? ", new String[]{String.valueOf(medicineId)});
        db.close();
    }
}
