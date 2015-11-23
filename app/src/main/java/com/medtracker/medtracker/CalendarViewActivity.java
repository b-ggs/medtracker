package com.medtracker.medtracker;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CalendarViewActivity extends AppCompatActivity {
    public static final String TAG = "CalendarViewActivity";

    ArrayList<Date> datesForCaldroid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_view);

        renderCaldroid();
    }

    @Override
    protected void onResume() {
        super.onResume();
        renderCaldroid();
    }

    public void renderCaldroid() {
        CaldroidFragment caldroidFragment = new CaldroidFragment();
        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        caldroidFragment.setArguments(args);

        datesForCaldroid = DateHelper.getDatesForCaldroid(getBaseContext());
        for(Date date : datesForCaldroid) {
            caldroidFragment.setTextColorForDate(R.color.darkred, date);
        }
        caldroidFragment.setCaldroidListener(caldroidListener);

        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendarLayout, caldroidFragment);
        t.commit();
    }

    CaldroidListener caldroidListener = new CaldroidListener() {
        @Override
        public void onSelectDate(Date date, View view) {
            DateFormat dateFormat = DateFormat.getDateTimeInstance();
            DateFormat dateFormat1 = DateFormat.getDateInstance();
            Log.d(TAG, String.format("%s", dateFormat.format(date)));
            if(datesForCaldroid.contains(date)) {
                //TODO: make new activity for medicine on this day
                Intent intent = new Intent();
                intent.putExtra("date", date);
                intent.setClass(getBaseContext(), ManageMedicinesActivity.class);
                startActivity(intent);
            } else {
               Toast.makeText(getBaseContext(), String.format("No medicine for %s.", dateFormat1.format(date)), Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calendar_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }
}
