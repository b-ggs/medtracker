package com.medtracker.medtracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import java.util.Date;
import java.util.HashMap;

public class EditMedicineActivity extends AppCompatActivity {
    private static final String TAG = "EditMedicineActivity";

    private int EDITING = 0;

    ArrayList<Calendar> times;
    Date startTime;
    Date startDate, endDate;

    Button setDateStartButton, setDateStartTodayButton, setDateEndButton, setDateEndTodayButton, setStartTimeButton, setTimeNowButton;
    TextView timesTextView, dateStartTextView, dateEndTextView, idTextView, startTimeTextView;

    EditText nameEditText, amountEditText, unitEditText, notesEditText, occurrencesEditText;

    ToggleButton suToggle, moToggle, tuToggle, weToggle, thToggle, frToggle, saToggle;
    HashMap<String, ToggleButton> daysToTakeToggles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_medicine);

        times = new ArrayList<>();

//        addTimeButton = (Button) findViewById(R.id.addTimeButton);
//        resetTimeButton = (Button) findViewById(R.id.resetTimeButton);
//        resetTimeButton.setOnClickListener(resetTimeListener);
        setStartTimeButton = (Button) findViewById(R.id.setStartTimeButton);
        setStartTimeButton.setOnClickListener(setStartTimeListener);

        timesTextView = (TextView) findViewById(R.id.timesTextView);

        setDateStartButton = (Button) findViewById(R.id.setDateStartButton);
        setDateStartButton.setOnClickListener(setDateStartListener);
        setDateStartTodayButton = (Button) findViewById(R.id.setDateStartTodayButton);
        setDateStartTodayButton.setOnClickListener(setDateStartTodayListener);

        setDateEndButton = (Button) findViewById(R.id.setDateEndButton);
        setDateEndButton.setOnClickListener(setDateEndListener);

        idTextView = (TextView) findViewById(R.id.medIdTextView);
        dateStartTextView = (TextView) findViewById(R.id.dateStartTextView);
        dateEndTextView = (TextView) findViewById(R.id.dateEndTextView);
        startTimeTextView = (TextView) findViewById(R.id.startTimeTextView);

        nameEditText = (EditText) findViewById(R.id.medNameEditText);
        amountEditText = (EditText) findViewById(R.id.amountEditText);
        unitEditText = (EditText) findViewById(R.id.unitEditText);
        notesEditText = (EditText) findViewById(R.id.notesEditText);
//        numberDaysEditText = (EditText) findViewById(R.id.numberDaysEditText);
        occurrencesEditText = (EditText) findViewById(R.id.occurrencesEditText);
        occurrencesEditText.addTextChangedListener(occurrenceEditTextWatcher);

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

            Log.d(TAG, medicineObject.toString());

            EDITING = 1;
            nameEditText.setText(medicineObject.getName());
            amountEditText.setText(String.valueOf(medicineObject.getDosageAmount()));
            unitEditText.setText(medicineObject.getDosageUnit());
            times = medicineObject.getTimes();
            startDate = medicineObject.getStartDate();
            endDate = medicineObject.getEndDate();
            startTime = medicineObject.getStartTime();
            occurrencesEditText.setText(String.valueOf(medicineObject.getOccurrences()));
            updateTimesField();
            updateDateFields();
            notesEditText.setText(medicineObject.getNotes());
            for(String day : medicineObject.getDaysToTake()) {
                daysToTakeToggles.get(day).setChecked(true);
            }
        } else {
            String id = String.valueOf(MedicineHelper.getNewId(getBaseContext()));
            idTextView.setText(id);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            startDate = calendar.getTime();
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            endDate = calendar.getTime();
            calendar.setTimeInMillis(System.currentTimeMillis());
            startTime = calendar.getTime();
            updateDateFields();
            updateTimesField();
        }
    }

    public boolean isValid() {
        boolean id = Integer.valueOf(idTextView.getText().toString()) > 0;
        boolean name = !nameEditText.getText().toString().equals("");
        boolean dosageAmount = !String.valueOf(amountEditText.getText().toString()).equals("");
        boolean dosageUnit = !unitEditText.getText().toString().equals("");
        boolean daysToTake = !MedicineHelper.sortDaysToTake(getDaysToTake()).isEmpty();
        boolean timesBoolean = !times.isEmpty();
        boolean startDateBoolean = startDate != null;
        boolean notes = !notesEditText.getText().toString().equals("");
        boolean occurrences = !occurrencesEditText.getText().toString().equals("");
        boolean dateCorrect = startDate.getTime() < endDate.getTime();

        return id && name && dosageAmount && dosageUnit && daysToTake && timesBoolean && startDateBoolean && notes && occurrences && dateCorrect;
    }

    public MedicineObject buildMedicineObject() {
        int id = Integer.valueOf(idTextView.getText().toString());
        String name = nameEditText.getText().toString();
        Float dosageAmount = Float.valueOf(amountEditText.getText().toString());
        String dosageUnit = unitEditText.getText().toString();
//        int numberDaysTake = Integer.valueOf(numberDaysEditText.getText().toString());
        ArrayList<String> daysToTake = MedicineHelper.sortDaysToTake(getDaysToTake());
        String notes = notesEditText.getText().toString();

        Calendar startDateCalendar = Calendar.getInstance();
        startDateCalendar.setTime(startDate);

        return new MedicineObject(id, name, dosageAmount, dosageUnit, daysToTake, times, getOccurrences(), startDate, endDate, startTime, notes);
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

    View.OnClickListener setStartTimeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            TimePickerDialog.newInstance(timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show(getFragmentManager(), "timePicker");
        }
    };

    View.OnClickListener setTimeNowListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Calendar calendar = Calendar.getInstance();
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTimeInMillis(System.currentTimeMillis());

            calendar.set(Calendar.HOUR_OF_DAY, calendar2.get(Calendar.HOUR_OF_DAY));
            calendar.set(Calendar.MINUTE, calendar2.get(Calendar.MINUTE));
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            startTime = calendar.getTime();
            updateTimes();
        }
    };

    TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(RadialPickerLayout radialPickerLayout, int hour, int minute) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            startDate = calendar.getTime();
            updateTimes();
        }
    };

    public int getOccurrences() {
        if(occurrencesEditText.getText().toString().length() <= 0) {
            occurrencesEditText.setText("1");
        }
        return Integer.valueOf(occurrencesEditText.getText().toString());
    }

    TextWatcher occurrenceEditTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if(!s.toString().equals(""))
                updateTimes();
        }
    };

    public void updateTimes() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        Calendar calendarEOD = Calendar.getInstance();
        calendarEOD.set(Calendar.HOUR_OF_DAY, 0);
        calendarEOD.set(Calendar.MINUTE, 0);
        calendarEOD.set(Calendar.SECOND, 0);
        calendarEOD.set(Calendar.MILLISECOND, 0);
        calendarEOD.add(Calendar.DAY_OF_MONTH, 1);

        long eodMillis = calendarEOD.getTimeInMillis();
        long calendarMillis = calendar.getTimeInMillis();
        long delayMillis = eodMillis - calendarMillis;

        DateFormat dateFormat = DateFormat.getTimeInstance();

        Log.d(TAG, String.format("%s %s", dateFormat.format(calendar.getTime()), dateFormat.format(calendarEOD.getTime())));
        Log.d(TAG, String.format("%s %s %s", eodMillis, calendarMillis, delayMillis));

        startTime = calendar.getTime();

        int occurrences = getOccurrences();
        long delayMillisDivided = delayMillis / occurrences;
        Log.d(TAG, String.format("Delay: %s", dateFormat.format(new Date(delayMillis))));
        Log.d(TAG, String.format("Delay Divided: %s", dateFormat.format(new Date(delayMillisDivided))));

        times.clear();
        for(int i = 1; i <= occurrences; i++) {
            Log.d(TAG, String.format("Time: %s", dateFormat.format(calendar.getTime())));
            Calendar calendarTemp = calendar.getInstance();
            calendarTemp.setTime(calendar.getTime());
            times.add(calendarTemp);
            Date date = calendar.getTime();
            calendar.setTimeInMillis(date.getTime() + delayMillisDivided);
        }
        updateTimesField();
    }

    public void updateTimesField() {
        DateFormat dateFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
        if(times.isEmpty()) {
            timesTextView.setText("No times added.");
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            for(Calendar time : times) {
                stringBuilder.append(dateFormat.format(time.getTime()) + " ");
//                String text = String.format("%s:%s", time.get(Calendar.HOUR), time.get(Calendar.MINUTE));
//                stringBuilder.append(text);
            }
            timesTextView.setText(stringBuilder.toString());
        }
        startTimeTextView.setText(dateFormat.format(startTime));
    }

    View.OnClickListener setDateStartListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            DatePickerDialog.newInstance(dateStartSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show(getFragmentManager(), "datePicker");
        }
    };

    View.OnClickListener setDateStartTodayListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            startDate = calendar.getTime();
            updateDateFields();
        }
    };

    View.OnClickListener setDateEndListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            DatePickerDialog.newInstance(dateEndSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show(getFragmentManager(), "datePicker");
        }
    };

    View.OnClickListener setDateEndTodayListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            endDate = calendar.getTime();
            updateDateFields();
        }
    };

    DatePickerDialog.OnDateSetListener dateStartSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            startDate = calendar.getTime();
            updateDateFields();
        }
    };

    DatePickerDialog.OnDateSetListener dateEndSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            endDate = calendar.getTime();
            updateDateFields();
        }
    };

    public void updateDateFields() {
        DateFormat dateFormat = DateFormat.getDateInstance();
        dateStartTextView.setText(dateFormat.format(startDate.getTime()));
        dateEndTextView.setText(dateFormat.format(endDate.getTime()));
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
