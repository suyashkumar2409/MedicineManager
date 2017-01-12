package com.example.suyashkumar.medicinescheduler;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.GridLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import static android.content.ContentValues.TAG;

/**
 * Created by SUYASH KUMAR on 1/9/2017.
 */

public class MedicineDisplayAdapter extends CursorAdapter {
    private LayoutInflater cursorInflater;
    private MedicineList medicineList;
    private int id;
    private Context ctx;
    public MedicineDisplayAdapter(Context context, Cursor c, int flags, MedicineList medicineList) {
        super(context, c, flags);
        cursorInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.medicineList = medicineList;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = cursorInflater.inflate(R.layout.medicine_layout, parent, false);
        id = cursor.getInt(cursor.getColumnIndex("_id"));
        ctx = context;
        return view;
    }

    public void deleteRecordWithId(int itemId)
    {
        MedicineSchedulerDatabaseHelper helper = new MedicineSchedulerDatabaseHelper(ctx);
        Log.v(TAG, String.valueOf(itemId));
        try{
            SQLiteDatabase db = helper.getWritableDatabase();
            db.delete(MedicineSchedulerDatabaseHelper.medicineTable, "_id = ?", new String[]{new Integer(itemId).toString()});
        }catch (SQLiteException e)
        {
            Toast.makeText(ctx, "Database Unavailable", Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    public void bindView(View view, Context context, final Cursor cursor) {
        CheckBox medicineName = (CheckBox)view.findViewById(R.id.medicine_checked);
        medicineName.setText(cursor.getString(cursor.getColumnIndex(MedicineSchedulerDatabaseHelper.MEDICINE_NAME)));
        medicineName.setChecked(cursor.getInt(cursor.getColumnIndex(MedicineSchedulerDatabaseHelper.ACTIVE))==1);

        medicineName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                medicineList.hasBeenChecked(cursor.getInt(cursor.getColumnIndex("_id")), isChecked);
            }
        });

        TextView durationChoice = (TextView)view.findViewById(R.id.medicine_duration_choice);
        String durationValue;
        int durationIntValue = cursor.getInt(cursor.getColumnIndex(MedicineSchedulerDatabaseHelper.FREQUENCY));
        switch(durationIntValue)
        {
            case 1:
                durationValue = "Daily";
                break;
            case 7:
                durationValue = "Weekly";
                break;
            case 14:
                durationValue = "Fortnightly";
                break;
            case 30:
                durationValue = "Monthly";
                break;
            default:
                durationValue = "Once in " + durationIntValue + " days";
        }
        durationChoice.setText(durationValue);

        Button button = (Button) view.findViewById(R.id.delete_medicine);
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                Log.d(TAG, "Button Click ");
                deleteRecordWithId(cursor.getInt(cursor.getColumnIndex("_id")));
                cursor.requery();
                notifyDataSetChanged();

                medicineList.checkIfEmpty();
            }
        });

        ListView timeOfDayList = (ListView)view.findViewById(R.id.medicine_time_of_day);

        int totalChecked = 0;
        if(cursor.getInt(cursor.getColumnIndex(MedicineSchedulerDatabaseHelper.MORNING)) == 1)
            totalChecked++;

        if(cursor.getInt(cursor.getColumnIndex(MedicineSchedulerDatabaseHelper.AFTERNOON)) == 1)
            totalChecked++;


        if(cursor.getInt(cursor.getColumnIndex(MedicineSchedulerDatabaseHelper.EVENING)) == 1)
            totalChecked++;


        if(cursor.getInt(cursor.getColumnIndex(MedicineSchedulerDatabaseHelper.NIGHT)) == 1)
            totalChecked++;

        String[] timeOfDayArray = new String[totalChecked];

        int idxTimeOfDay = 0;

        if(cursor.getInt(cursor.getColumnIndex(MedicineSchedulerDatabaseHelper.MORNING)) == 1)
            timeOfDayArray[idxTimeOfDay++] = "MORNING";

        if(cursor.getInt(cursor.getColumnIndex(MedicineSchedulerDatabaseHelper.AFTERNOON)) == 1)
            timeOfDayArray[idxTimeOfDay++] = "AFTERNOON";


        if(cursor.getInt(cursor.getColumnIndex(MedicineSchedulerDatabaseHelper.EVENING)) == 1)
            timeOfDayArray[idxTimeOfDay++] = "EVENING";


        if(cursor.getInt(cursor.getColumnIndex(MedicineSchedulerDatabaseHelper.NIGHT)) == 1)
            timeOfDayArray[idxTimeOfDay++] = "NIGHT";


        ArrayAdapter<String> timeOfDayDuration = new ArrayAdapter<String>(context, R.layout.medicine_time_of_day, timeOfDayArray);
        timeOfDayList.setAdapter(timeOfDayDuration);
        String pillDirections = cursor.getString(cursor.getColumnIndex(MedicineSchedulerDatabaseHelper.DIRECTIONS));
        TextView pillDirectionsView = (TextView)view.findViewById(R.id.medicine_pill_directions);

        if(pillDirections.isEmpty())
        {
            pillDirectionsView.setVisibility(View.GONE);
        }
        else
        {
            pillDirectionsView.setText(view.getResources().getString(R.string.pill_directions) + " " + pillDirections);
        }
    }
}
