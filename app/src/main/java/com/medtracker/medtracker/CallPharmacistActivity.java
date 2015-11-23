package com.medtracker.medtracker;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class CallPharmacistActivity extends AppCompatActivity {

    Button jesseButton, iraButton, claudineButton, berleButton, vanessaButton;
    String[] numbers = {
            "+63 977 394 1801",
            "+63 915 664 8665",
            "+63 917 510 6580",
            "+63 999 883 6149",
            "+63 917 577 8116"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_pharmacist);

        jesseButton = (Button) findViewById(R.id.jesseButton);
        jesseButton.setOnClickListener(jesseListener);
        iraButton = (Button) findViewById(R.id.iraButton);
        iraButton.setOnClickListener(iraListener);
        claudineButton = (Button) findViewById(R.id.claudineButton);
        claudineButton.setOnClickListener(claudineListener);
        berleButton = (Button) findViewById(R.id.berleButton);
        berleButton.setOnClickListener(berleListener);
        vanessaButton = (Button) findViewById(R.id.vanessaButton);
        vanessaButton.setOnClickListener(vanessaListener);
    }

    public void call(int id) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(String.format("tel:%s", numbers[id].replace(" ", ""))));
        startActivity(intent);
    }

    View.OnClickListener jesseListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            call(0);
        }
    };

    View.OnClickListener iraListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            call(1);
        }
    };

    View.OnClickListener claudineListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            call(2);
        }
    };

    View.OnClickListener berleListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            call(3);
        }
    };

    View.OnClickListener vanessaListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            call(4);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_call_pharmacist, menu);
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
