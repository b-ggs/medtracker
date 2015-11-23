package com.medtracker.medtracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.datetimepicker.date.DatePickerDialog;
import com.android.datetimepicker.time.RadialPickerLayout;
import com.android.datetimepicker.time.TimePickerDialog;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class EditMedicineActivity extends AppCompatActivity {
    private static final String TAG = "EditMedicineActivity";

    private int EDITING = 0;

    ArrayList<Calendar> times;
    Calendar startDate;

    Button addTimeButton, resetTimeButton, setDateButton, setDateTodayButton, saveButton;
    TextView timesTextView, dateTextView, idTextView;

    EditText nameEditText, amountEditText, unitEditText, notesEditText, numberDaysEditText;

    ToggleButton suToggle, moToggle, tuToggle, weToggle, thToggle, frToggle, saToggle;
    HashMap<String, ToggleButton> daysToTakeToggles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_medicine);

        times = new ArrayList<>();
        startDate = null;

        addTimeButton = (Button) findViewById(R.id.addTimeButton);
        addTimeButton.setOnClickListener(addTimeListener);
        resetTimeButton = (Button) findViewById(R.id.resetTimeButton);
        resetTimeButton.setOnClickListener(resetTimeListener);
        timesTextView = (TextView) findViewById(R.id.timesTextView);

        setDateButton = (Button) findViewById(R.id.setDateButton);
        setDateButton.setOnClickListener(setDateListener);
        setDateTodayButton = (Button) findViewById(R.id.setDateTodayButton);
        setDateTodayButton.setOnClickListener(setDateTodayListener);
        dateTextView = (TextView) findViewById(R.id.dateTextView);

        idTextView = (TextView) findViewById(R.id.medIdTextView);
        nameEditText = (EditText) findViewById(R.id.medNameEditText);
        amountEditText = (EditText) findViewById(R.id.amountEditText);
        unitEditText = (EditText) findViewById(R.id.unitEditText);
        notesEditText = (EditText) findViewById(R.id.notesEditText);
        numberDaysEditText = (EditText) findViewById(R.id.numberDaysEditText);

        suToggle = (ToggleButton) findViewById(R.id.suToggle);
        moToggle = (ToggleButton) findViewById(R.id.moToggle);
        tuToggle = (ToggleButton) findViewById(R.id.tuToggle);
        weToggle = (ToggleButton) findViewById(R.id.weToggle);
        thToggle = (ToggleButton) findViewById(R.id.thToggle);
        frToggle = (ToggleButton) findViewById(R.id.frToggle);
        saToggle = (ToggleButton) findViewById(R.id.saToggle);

        daysToTakeToggles = new HashMap<>();
        daysToTakeToggles.put("Su", suToggle);
        daysToTakeToggles.put("Mo", moToggle);
        daysToTakeToggles.put("Tu", tuToggle);
        daysToTakeToggles.put("We", weToggle);
        daysToTakeToggles.put("Th", thToggle);
        daysToTakeToggles.put("Fr", frToggle);
        daysToTakeToggles.put("Sa", saToggle);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            String id = String.valueOf(extras.get("id"));
            MedicineObject medicineObject = MedicineHelper.getMedicine(getBaseContext(), Integer.valueOf(id));
            idTextView.setText(id);

            EDITING = 1;
            nameEditText.setText(medicineObject.getName());
            amountEditText.setText(String.valueOf(medicineObject.getDosageAmount()));
            unitEditText.setText(medicineObject.getDosageUnit());
            numberDaysEditText.setText(String.valueOf(medicineObject.getNumberDaysTake()));
            times = medicineObject.getTimes();
            updateTimesField();
            startDate = medicineObject.getStartDate();
            updateDateField();
            notesEditText.setText(medicineObject.getNotes());
            for(String day : medicineObject.getDaysToTake()) {
                daysToTakeToggles.get(day).setChecked(true);
            }
        } else {
            String id = String.valueOf(MedicineHelper.getNewId(getBaseContext()));
            idTextView.setText(id);
        }
    }

    public boolean isValid() {
        boolean id = Integer.valueOf(idTextView.getText().toString()) > 0;
        boolean name = !nameEditText.getText().toString().equals("");
        boolean dosageAmount = !String.valueOf(amountEditText.getText().toString()).equals("");
        boolean dosageUnit = !unitEditText.getText().toString().equals("");
        boolean daysToTake = !MedicineHelper.sortDaysToTake(getDaysToTake()).isEmpty();
        boolean numberDaysTake = !numberDaysEditText.getText().toString().equals("");
        boolean timesBoolean = !times.isEmpty();
        boolean startDateBoolean = startDate != null;
        boolean notes = !notesEditText.getText().toString().equals("");

        return id && name && numberDaysTake && dosageAmount && dosageUnit && daysToTake && timesBoolean && startDateBoolean && notes;
    }

    public MedicineObject buildMedicineObject() {
        int id = Integer.valueOf(idTextView.getText().toString());
        String name = nameEditText.getText().toString();
        Float dosageAmount = Float.valueOf(amountEditText.getText().toString());
        String dosageUnit = unitEditText.getText().toString();
        int numberDaysTake = Integer.valueOf(numberDaysEditText.getText().toString());
        ArrayList<String> daysToTake = MedicineHelper.sortDaysToTake(getDaysToTake());
        String notes = notesEditText.getText().toString();

        return new MedicineObject(id, name, dosageAmount, dosageUnit, numberDaysTake, daysToTake, times, startDate, notes);
    }

    public ArrayList<String> getDaysToTake() {
        ArrayList<String> daysToTake = new ArrayList<>();
        for(Object keyObject: daysToTakeToggles.keySet()) {
            String key = (String) keyObject;
            if(daysToTakeToggles.get(key).isChecked()) {
                daysToTake.add(key);
            }
        }
        return daysToTake;
    }

    View.OnClickListener addTimeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            TimePickerDialog.newInstance(timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show(getFragmentManager(), "timePicker");
        }
    };

    View.OnClickListener resetTimeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            times.clear();
            updateTimesField();
        }
    };

    TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(RadialPickerLayout radialPickerLayout, int hour, int minute) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            times.add(calendar);
            updateTimesField();
        }
    };

    public void updateTimesField() {
        if(times.isEmpty()) {
            timesTextView.setText("No times added.");
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            for(Calendar time : times) {
                DateFormat dateFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
                stringBuilder.append(dateFormat.format(time.getTime()) + " ");
//                String text = String.format("%s:%s", time.get(Calendar.HOUR), time.get(Calendar.MINUTE));
//                stringBuilder.append(text);
            }
            timesTextView.setText(stringBuilder.toString());
        }
    }

    View.OnClickListener setDateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            DatePickerDialog.newInstance(dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show(getFragmentManager(), "datePicker");
        }
    };

    View.OnClickListener setDateTodayListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            startDate = calendar;
            updateDateField();
        }
    };

    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            startDate = calendar;
            updateDateField();
        }
    };

    public void updateDateField() {
        DateFormat dateFormat = DateFormat.getDateInstance();
        dateTextView.setText(dateFormat.format(startDate.getTime()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_medicine, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {
            if (isValid()) {
                if(EDITING == 1)
                    MedicineHelper.removeMedicine(getBaseContext(), Integer.valueOf(idTextView.getText().toString()));
                MedicineHelper.addMedicine(getBaseContext(), buildMedicineObject());
                Toast.makeText(getBaseContext(), "Successfully saved.", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(getBaseContext(), "Error saving medicine.", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.action_delete) {
            if(EDITING == 1)
                MedicineHelper.removeMedicine(getBaseContext(), Integer.valueOf(idTextView.getText().toString()));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
