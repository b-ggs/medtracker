package com.medtracker.medtracker;

/**
 * Created by boggs on 11/23/15.
 */
public class PendingIntentObject {
    private int id;
    private int requestCode;
    private int dateId;
    private int medicineId;

    public PendingIntentObject(int id, int requestCode, int dateId, int medicineId) {
        this.id = id;
        this.requestCode = requestCode;
        this.dateId = dateId;
        this.medicineId = medicineId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    public int getDateId() {
        return dateId;
    }

    public void setDateId(int dateId) {
        this.dateId = dateId;
    }

    public int getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(int medicineId) {
        this.medicineId = medicineId;
    }

    public String toString() {
        return String.format("id: %s, rc: %s, dId: %s, mId: %s", id, requestCode, dateId, medicineId);
    }
}
