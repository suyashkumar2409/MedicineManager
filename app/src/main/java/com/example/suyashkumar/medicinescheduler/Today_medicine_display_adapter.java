package com.example.suyashkumar.medicinescheduler;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by SUYASH KUMAR on 1/10/2017.
 */

//THis shouldnt be a cursor Adapter
    //Create a new transaction table that includes - _id of med, takenDate, morning, af, ev, ni
public class Today_medicine_display_adapter extends CursorAdapter {
    private LayoutInflater cursorInflater;

    public Today_medicine_display_adapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
        cursorInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return cursorInflater.inflate(R.layout.today_medicine_layout, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ((CheckBox)view.findViewById(R.id.medicine_name)).setText(cursor.getString(cursor.getColumnIndex(MedicineSchedulerDatabaseHelper.MEDICINE_NAME)));
        ((CheckBox)view.findViewById(R.id.medicine_name)).setText(cursor.getString(cursor.getColumnIndex(MedicineSchedulerDatabaseHelper.MEDICINE_NAME)));
    }
}
