package com.example.suyashkumar.medicinescheduler;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.nfc.Tag;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suyashkumar.medicinescheduler.CalendarHandler;
import com.example.suyashkumar.medicinescheduler.MedicineSchedulerDatabaseHelper;
import com.example.suyashkumar.medicinescheduler.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TopActivity extends Fragment {
    private MedicineSchedulerDatabaseHelper helper;
    private Context ctx;
    private SQLiteDatabase db;
    private Cursor cursor;
    private View view;
    private Calendar calendar;

    public TopActivity() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_top, container, false);
        ctx = getActivity().getApplicationContext();
        TextView todaysDate = (TextView)view.findViewById(R.id.todaysDate);
        calendar = Calendar.getInstance();

        todaysDate.setText(calendar.get(calendar.DATE) + " " + CalendarHandler.numberToMonth(calendar.get(calendar.MONTH)) + "\n" + CalendarHandler.numToDay(calendar.get(calendar.DAY_OF_WEEK)));

        helper = new MedicineSchedulerDatabaseHelper(ctx);


        return view;
    }

    public void refreshData()
    {
        try{
            db = helper.getWritableDatabase();
            cursor = db.query(MedicineSchedulerDatabaseHelper.medicineTable,
                    new String[]{"_id", helper.MEDICINE_NAME, helper.START_DATE, helper.FREQUENCY, helper.MORNING, helper.AFTERNOON, helper.EVENING,
                            helper.NIGHT, helper.DIRECTIONS},
                    "ACTIVE=?",
                    new String[]{new Integer(1).toString()},
                    null, null, null
            );
        }
        catch(SQLiteException e)
        {
            Toast.makeText(ctx, "Database Unavailable", Toast.LENGTH_SHORT).show();
        }

        ArrayList<Medicine> morningList = new ArrayList<Medicine>();
        ArrayList<Medicine> afternoonList = new ArrayList<Medicine>();
        ArrayList<Medicine> eveningList = new ArrayList<Medicine>();
        ArrayList<Medicine> nightList = new ArrayList<Medicine>();

        if(cursor.moveToFirst())
        {
            do{
                String startDate = cursor.getString(cursor.getColumnIndex(helper.START_DATE));
                int frequency = cursor.getInt(cursor.getColumnIndex(helper.FREQUENCY));
                int differenceDays = CalendarHandler.getDifferenceInDays(calendar.getTimeInMillis(), startDate);

                if(differenceDays % frequency == 0)
                {
                    if(cursor.getInt(cursor.getColumnIndex(helper.MORNING))== 1)
                    {
                        morningList.add(new Medicine(cursor.getString(cursor.getColumnIndex(helper.MEDICINE_NAME)),
                                cursor.getString(cursor.getColumnIndex(helper.DIRECTIONS))));
                    }


                    if(cursor.getInt(cursor.getColumnIndex(helper.AFTERNOON))== 1)
                    {
                        afternoonList.add(new Medicine(cursor.getString(cursor.getColumnIndex(helper.MEDICINE_NAME)),
                                cursor.getString(cursor.getColumnIndex(helper.DIRECTIONS))));
                    }


                    if(cursor.getInt(cursor.getColumnIndex(helper.EVENING))== 1)
                    {
                        eveningList.add(new Medicine(cursor.getString(cursor.getColumnIndex(helper.MEDICINE_NAME)),
                                cursor.getString(cursor.getColumnIndex(helper.DIRECTIONS))));
                    }


                    if(cursor.getInt(cursor.getColumnIndex(helper.NIGHT))== 1)
                    {
                        nightList.add(new Medicine(cursor.getString(cursor.getColumnIndex(helper.MEDICINE_NAME)),
                                cursor.getString(cursor.getColumnIndex(helper.DIRECTIONS))));
                    }
                }

            }while(cursor.moveToNext());
        }

        if(morningList.isEmpty())
        {
            ((TextView)view.findViewById(R.id.morning_marker)).setVisibility(View.GONE);
        }
        else
        {
            ((TextView)view.findViewById(R.id.morning_marker)).setVisibility(View.VISIBLE);

        }
    }

}
