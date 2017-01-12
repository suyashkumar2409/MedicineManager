package com.example.suyashkumar.medicinescheduler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MedicineList extends Fragment implements Activeable, Deletable{
    private MedicineDisplayAdapter adapter;
    private ListView listView;
    private MedicineSchedulerDatabaseHelper helper;
    private SQLiteDatabase db;
    private Cursor cursor;
    private Context ctx;
    private View view;
    public MedicineList() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_medicine_list, container, false);
        ctx = getActivity().getApplicationContext();
        helper = new MedicineSchedulerDatabaseHelper(ctx);
        listView = (ListView)view.findViewById(R.id.medicine_list);


//        createCursorAndAdapter();

        return view;
    }

    @Override
    public void hasBeenChecked(int id, boolean active) {
        SQLiteDatabase db;
        try{
            db = helper.getWritableDatabase();
            ContentValues value = new ContentValues();
            value.put(MedicineSchedulerDatabaseHelper.ACTIVE, active);

            db.update(MedicineSchedulerDatabaseHelper.medicineTable,
                    value,
                    "_id = ?",
                    new String[]{new Integer(id).toString()});

            db.close();
        }catch (SQLiteException e)
        {
            Toast.makeText(ctx, "Database Unavailable", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(cursor!=null)
        cursor.close();
        if(db!=null)
        db.close();
    }

    public void createCursorAndAdapter(){
        try{
            db = helper.getReadableDatabase();
            cursor = db.query(MedicineSchedulerDatabaseHelper.medicineTable,
                    new String[] {"_id", helper.MEDICINE_NAME, helper.ACTIVE, helper.FREQUENCY, helper.MORNING, helper.AFTERNOON
                            , helper.EVENING, helper.NIGHT, helper.DIRECTIONS},
                    null, null, null, null, helper.MEDICINE_NAME);

            if(cursor.moveToFirst()) {
                adapter = new MedicineDisplayAdapter(ctx, cursor, 0, this);
                listView.setAdapter(adapter);
            }
            else
            {
                ((TextView)view.findViewById(R.id.no_medicine)).setVisibility(View.VISIBLE);
            }

        }catch (SQLiteException e)
        {
            Log.e("ERRor:",e.toString());
            Toast.makeText(ctx, "Database Unavailable", Toast.LENGTH_SHORT).show();
        }

    }

    public void onResume(){
        super.onResume();
//        Toast.makeText(ctx, "onResum Called", Toast.LENGTH_SHORT).show();
        createCursorAndAdapter();
        checkIfEmpty();
    }

    @Override
    public void checkIfEmpty() {
        if(cursor == null || (!cursor.moveToFirst()))
        {
            ((TextView)view.findViewById(R.id.no_medicine)).setVisibility(View.VISIBLE);
        }
        else
        {
            ((TextView)view.findViewById(R.id.no_medicine)).setVisibility(View.GONE);
        }
    }
}
