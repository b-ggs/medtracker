package com.medtracker.medtracker;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by boggs on 11/22/15.
 */
public class DateObject {
    private int id;
    private long millis;
    private Date date;
    private int medicineId;

    public DateObject(int id, long millis, int medicineId) {
        this.id = id;
        this.millis = millis;
        this.medicineId = medicineId;
        this.date = getDateOnly();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getMillis() {
        return millis;
    }

    public void setMillis(long millis) {
        this.millis = millis;
    }

    public int getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(int medicineId) {
        this.medicineId = medicineId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDateOnly() {
        Calendar calendar = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        calendar2.setTimeInMillis(0);

        calendar2.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
        calendar2.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
        calendar2.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
        calendar2.set(Calendar.MILLISECOND, 0);
        calendar2.set(Calendar.HOUR, 0);
        calendar2.set(Calendar.MINUTE, 0);
        calendar2.set(Calendar.SECOND, 0);

        return calendar2.getTime();
    }
}
