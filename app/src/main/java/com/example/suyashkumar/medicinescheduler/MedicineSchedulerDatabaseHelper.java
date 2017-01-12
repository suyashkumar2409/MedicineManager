package com.example.suyashkumar.medicinescheduler;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by SUYASH KUMAR on 1/8/2017.
 */

public class MedicineSchedulerDatabaseHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "medicineScheduler";
    public static final int DB_VERSION = 1;

    public static final String medicineTable = "MEDICINE";

    public static final String MEDICINE_NAME = "MEDICINE_NAME";
    public static final String FREQUENCY = "FREQUENCY";
    public static final String MORNING = "MORNING";
    public static final String AFTERNOON = "AFTERNOON";
    public static final String EVENING = "EVENING";
    public static final String NIGHT = "NIGHT";
    public static final String START_DATE = "START_DATE";
    public static final String DURATION = "DURATION";
    public static final String DIRECTIONS = "DIRECTIONS";
    public static final String INVENTORY = "INVENTORY";
    public static final String ACTIVE = "ACTIVE";

    private Context ctx;


    MedicineSchedulerDatabaseHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
        this.ctx = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        updateMyDatabase(db, 0, DB_VERSION);
        Toast.makeText(ctx, "Database Created", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateMyDatabase(db, oldVersion, newVersion);
        Toast.makeText(ctx, "Database Upgraded", Toast.LENGTH_SHORT).show();
    }

    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion){
        if(oldVersion < 1)
        {
            db.execSQL("CREATE TABLE "+medicineTable+" ( _id INTEGER PRIMARY KEY AUTOINCREMENT,  "
            + "MEDICINE_NAME TEXT, "
            + "FREQUENCY INTEGER, "
            + "MORNING INTEGER, "
            + "AFTERNOON INTEGER, "
            + "EVENING INTEGER, "
            + "NIGHT INTEGER, "
            + "START_DATE TEXT, "
            + "DURATION INTEGER, "
            + "DIRECTIONS TEXT, "
            + "INVENTORY INT, "
            + "ACTIVE INTEGER);");
        }

    }

    public void insertMedicine(SQLiteDatabase db, String name, int frequency, int morning, int afternoon, int evening, int night
    , String start_date, int duration, String directions, int inventory, int active)
    {
        ContentValues value = new ContentValues();
        value.put("MEDICINE_NAME", name);
        value.put("FREQUENCY", frequency);
        value.put("MORNING", morning);
        value.put("AFTERNOON", afternoon);
        value.put("EVENING", evening);
        value.put("NIGHT", night);
        value.put("START_DATE", start_date);
        value.put("DURATION", duration);
        value.put("DIRECTIONS", directions);
        value.put("INVENTORY", inventory);
        value.put("ACTIVE", active);

        db.insert(medicineTable, null, value);
    }
}
