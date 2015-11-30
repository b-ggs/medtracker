package com.medtracker.medtracker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by boggs on 11/22/15.
 */
public class MedicineObject {
    private int id;
    private String name;
    private float dosageAmount;
    private String dosageUnit;
    private ArrayList<String> daysToTake;
    private ArrayList<Calendar> times;
    private int occurrences;
    private Date startDate;
    private Date endDate;
    private Date startTime;
    private String notes;

    public MedicineObject(int id, String name, float dosageAmount, String dosageUnit, ArrayList<String> daysToTake, ArrayList<Calendar> times, int occurrences, Date startDate, Date endDate, Date startTime, String notes) {
        this.id = id;
        this.name = name;
        this.dosageAmount = dosageAmount;
        this.dosageUnit = dosageUnit;
        this.daysToTake = daysToTake;
        this.times = times;
        this.occurrences = occurrences;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.notes = notes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getDosageAmount() {
        return dosageAmount;
    }

    public void setDosageAmount(float dosageAmount) {
        this.dosageAmount = dosageAmount;
    }

    public String getDosageUnit() {
        return dosageUnit;
    }

    public void setDosageUnit(String dosageUnit) {
        this.dosageUnit = dosageUnit;
    }

    public ArrayList<String> getDaysToTake() {
        return daysToTake;
    }

    public void setDaysToTake(ArrayList<String> daysToTake) {
        this.daysToTake = daysToTake;
    }

    public ArrayList<Calendar> getTimes() {
        return times;
    }

    public void setTimes(ArrayList<Calendar> times) {
        this.times = times;
    }

    public int getOccurrences() {
        return occurrences;
    }

    public void setOccurrences(int occurrences) {
        this.occurrences = occurrences;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("---\n");
        stringBuilder.append(String.format("id %s\n", getId()));
        stringBuilder.append(String.format("name %s\n", getName()));
        stringBuilder.append(String.format("dosageAmount %s\n", getDosageAmount()));
        stringBuilder.append(String.format("dosageUnit %s\n", getDosageUnit()));
        stringBuilder.append(String.format("times %s\n", getTimes()));
        stringBuilder.append(String.format("occurrences %s\n", getOccurrences()));
        stringBuilder.append(String.format("startDate %s\n", getStartDate()));
        stringBuilder.append(String.format("endDate %s\n", getEndDate()));
        stringBuilder.append(String.format("daysToTake %s\n", getDaysToTake()));
        stringBuilder.append(String.format("notes %s\n", getNotes()));
        return stringBuilder.toString();
    }
}
