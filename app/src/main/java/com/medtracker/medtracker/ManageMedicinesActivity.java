package com.medtracker.medtracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ManageMedicinesActivity extends AppCompatActivity {
    public static final String TAG = "ManageMedicinesActivity";

    Button addMedicineButton;
    ListView medicineListView;
    Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_medicines);

        extras = getIntent().getExtras();
        populateMedicineList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateMedicineList();
    }

    public void populateMedicineList() {
        if(extras != null) {
            Date date = (Date) extras.get("date");
            DateFormat dateFormat = DateFormat.getDateInstance();
            setTitle(String.format("Medicine on %s", dateFormat.format(date)));
            populateListView(MedicineHelper.getAllMedicineFromDateObjects(getBaseContext(), DateHelper.getDatesFromDateOnly(getBaseContext(), date)));
        } else {
            populateListView(MedicineHelper.getAllMedicines(getBaseContext()));
        }
    }

    public void populateListView(ArrayList<MedicineObject> medicineList) {
        MedicineListAdapter medicineListAdapter = new MedicineListAdapter(this, medicineList);
        medicineListView = (ListView) findViewById(R.id.medicineListView);
        medicineListView.setAdapter(medicineListAdapter);
        medicineListView.setOnItemClickListener(itemClickListener);
    }

    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            MedicineObject medicineObject = (MedicineObject) parent.getItemAtPosition(position);
            Intent intent = new Intent();
            intent.setClass(getBaseContext(), EditMedicineActivity.class);
            intent.putExtra("id", medicineObject.getId());
            startActivity(intent);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_manage_medicines, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            Intent intent = new Intent();
            intent.setClass(getBaseContext(), EditMedicineActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
