package com.medtracker.medtracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button manageMedicinesButton, calendarViewButton, callPharmacistButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        manageMedicinesButton = (Button) findViewById(R.id.manageMedicinesButton);
        manageMedicinesButton.setOnClickListener(manageMedicinesListener);
        calendarViewButton = (Button) findViewById(R.id.calendarViewButton);
        calendarViewButton.setOnClickListener(calendarViewListener);
        callPharmacistButton = (Button) findViewById(R.id.callPharmacistButton);
        callPharmacistButton.setOnClickListener(callPharmacistListener);
    }

    View.OnClickListener callPharmacistListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(getBaseContext(), CallPharmacistActivity.class);
            startActivity(intent);
        }
    };

    View.OnClickListener manageMedicinesListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(getBaseContext(), ManageMedicinesActivity.class);
            startActivity(intent);
        }
    };

    View.OnClickListener calendarViewListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(getBaseContext(), CalendarViewActivity.class);
            startActivity(intent);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }
}
