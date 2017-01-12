package com.example.suyashkumar.medicinescheduler;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class NewMedicine extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_medicine);

        setDateClicking();

        getActionBar().setDisplayHomeAsUpEnabled(true);

//        ********* Spinner event code *********

        final Spinner frequencySpinner = (Spinner)findViewById(R.id.medicine_frequency_options_1);
        frequencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 6)
                {
                    ((LinearLayout)findViewById(R.id.medicine_frequency_options_2)).setVisibility(View.VISIBLE);
                }
                else
                {
                    ((LinearLayout)findViewById(R.id.medicine_frequency_options_2)).setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                frequencySpinner.setSelection(0);
            }
        });

//        ******** RadioButton event code ***********
        // THis isnt working properly
        final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.medicine_duration_choice);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                View radioButton = radioGroup.findViewById(checkedId);
                int idx = radioGroup.indexOfChild(radioButton);

                Log.e("HERE", String.valueOf(checkedId));
                if(idx == 0)
                {
                    ((EditText)findViewById(R.id.duration_days)).setVisibility(View.GONE);
                }
                else
                {
                    ((EditText)findViewById(R.id.duration_days)).setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void setDateClicking()
    {
        final EditText editText = (EditText)findViewById(R.id.medicine_date_picker);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog.OnDateSetListener dpd = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {

                        int s=monthOfYear+1;
                        String a = dayOfMonth+"/"+s+"/"+year;
                        editText.setText(""+a);
                    }
                };

                Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
                int seconds = c.get(Calendar.SECOND);
                DatePickerDialog d = new DatePickerDialog(NewMedicine.this, dpd, c.getTime().getMonth(), c.getTime().getDate(), c.getTime().getYear() );
                d.show();

            }
        });
    }

//    private int
    public void submitMedicine(View view){
        String name = ((EditText)findViewById(R.id.medicine_medicine_name)).getText().toString();
        boolean isValid = true;
        if(name.isEmpty())
        {
            isValid = false;
            ((TextView)findViewById(R.id.name_essential)).setVisibility(View.VISIBLE);
        }
        else
        {
            ((TextView)findViewById(R.id.name_essential)).setVisibility(View.GONE);
        }


        int frequency;
        switch(((Spinner)findViewById(R.id.medicine_frequency_options_1)).getSelectedItemPosition())
        {
            case 0:
                frequency = 1;
                break;
            case 1:
                frequency = 2;
                break;
            case 2:
                frequency = 3;
                break;
            case 4:
                frequency = 7;
                break;
            case 5:
                frequency = 14;
                break;
            case 6:
                frequency = 30;
                break;
            case 7:
                String frequencyString = ((EditText)findViewById(R.id.medicine_every_dash_days)).getText().toString();
                if(frequencyString.isEmpty())
                   frequency = 0;
                else
                    frequency = Integer.parseInt(frequencyString);
                break;
            default:
                frequency = 1;
        }

        int morning_true, afternoon_true, evening_true, night_true;

        morning_true = getDailyIntervalValue(R.id.medicine_checkbox_morning);
        afternoon_true = getDailyIntervalValue(R.id.medicine_checkbox_afternoon);
        evening_true = getDailyIntervalValue(R.id.medicine_checkbox_evening);
        night_true = getDailyIntervalValue(R.id.medicine_checkbox_night);

        if((morning_true == 0) && (afternoon_true == 0) && (evening_true == 0) && (night_true == 0))
        {
            ((TextView)findViewById(R.id.frequency_essential)).setVisibility(View.VISIBLE);
            isValid = false;
        }
        else
        {
            ((TextView)findViewById(R.id.frequency_essential)).setVisibility(View.GONE);
        }
        String startDate = ((EditText)findViewById(R.id.medicine_date_picker)).getText().toString();

        int durationDays=0;
//        duration days code using radiogroup
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.medicine_duration_choice);
        int checkedId = radioGroup.getCheckedRadioButtonId();
        View radioButton = radioGroup.findViewById(checkedId);
        int radioIdx = radioGroup.indexOfChild(radioButton);

        if(radioIdx == 0)
            durationDays = -1;
        else
        {
            String daysText = ((EditText)findViewById(R.id.medicine_every_dash_days)).getText().toString();
            if(daysText.isEmpty())
                durationDays = 0;
            else
                durationDays = Integer.parseInt(daysText);
        }
//        picture picker code

//        String programName = ((EditText)findViewById(R.id.medicine_program_name)).getText().toString();
        String directions = ((EditText)findViewById(R.id.medicine_directions)).getText().toString();

        int inventory;
        String inventoryString = ((EditText)findViewById(R.id.medicine_current_inventory)).getText().toString();
        if(inventoryString.isEmpty())
            inventory = 0;
        else
            inventory = Integer.parseInt(inventoryString);

        if(isValid)
        {
            MedicineSchedulerDatabaseHelper helper = new MedicineSchedulerDatabaseHelper(this);

            try{
                SQLiteDatabase db = helper.getWritableDatabase();
                helper.insertMedicine(db, name, frequency, morning_true, afternoon_true, evening_true, night_true,
                        startDate, durationDays, directions, inventory, 1);

            }
            catch(SQLiteException e)
            {
                Toast.makeText(this, "Database Unavailable", Toast.LENGTH_SHORT).show();
            }
            finish();
        }
        else
        {
            Toast.makeText(this,"Not valid",Toast.LENGTH_SHORT).show();
        }
    }

    private int getDailyIntervalValue(int id)
    {
        if(((CheckBox)findViewById(id)).isChecked())
            return 1;
        else
            return 0;
    }
}
