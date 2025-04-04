package com.example.mscarealpha.ui.symptoms;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public class SymptomDbHelper {
    // Database information
    private SQLiteDatabase db;

    /*
       Next we have a public static final string for
       each row/table that we need to refer to both
       inside and outside this class
   */

    // Column names
    public static final String TABLE_ROW_ID = "_id";
    public static final String TABLE_ROW_BODY_PART = "bodyPart";
    public static final String TABLE_ROW_SYMPTOM_NAME = "symptomName";
    public static final String TABLE_ROW_PAIN_LEVEL = "painLevel";
    public static final String TABLE_ROW_NOTES = "notes";
    public static final String TABLE_ROW_TIMESTAMP = "timestamp";


    /*
        Next we have a private static final strings for
        each row/table that we need to refer to just
        inside this class
    */

    private static final String DB_NAME = "symptoms_db";
    private static final int DB_VERSION = 1;
    private static final String TABLE_SYMPTOMS = "symptoms";

    // Constructor to Setup our DataManager
    public SymptomDbHelper(Context context){

        // STEP 2.
        // Create an instance of our internal CustomSQLiteOpenHelper

        CustomSQLiteOpenHelper helper = new CustomSQLiteOpenHelper(context);

        // STEP 3.
        // Get a writable database
        // if the database doesn't exist yet
        db = helper.getWritableDatabase(); // We can now use this database to access/ query the data
    }

    // Create a SQLiteOpenHelper subclass
    // This is created when our DataManager is initialized

    private class CustomSQLiteOpenHelper extends SQLiteOpenHelper {

        // If the version is newer than the DB file version
        // this will trigger the onUpgrade()
        public CustomSQLiteOpenHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        // This runs the first time the database is created
        @Override
        public void onCreate(SQLiteDatabase db) {

            // We code whatever is needed to configure/ create the database
            // We are creating a table for the symptoms records
            String newTableQueryString = "CREATE TABLE " + TABLE_SYMPTOMS + " ("
                    + TABLE_ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                    + TABLE_ROW_BODY_PART + " TEXT NOT NULL, "
                    + TABLE_ROW_SYMPTOM_NAME + " TEXT NOT NULL, "
                    + TABLE_ROW_PAIN_LEVEL + " INTEGER NOT NULL, "
                    + TABLE_ROW_NOTES + " TEXT, "
                    + TABLE_ROW_TIMESTAMP + " TEXT NOT NULL);";


            db.execSQL(newTableQueryString);
        }

        // This method only runs when we increment DB_VERSION
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // Not needed in this app
            // but we must still override it
            // We can code whatever is needed to convert the old db to the new db
            // drop tables and recreate them with new columns etc...

        }
    }

    // Step 4. Write any helper methods we want to manage the data access
    // insert / select / delete etc..

    // Insert a symptom record
    public void insert(Symptom symptom) {
        String query = "INSERT INTO " + TABLE_SYMPTOMS + " (" +
                TABLE_ROW_BODY_PART + ", " +
                TABLE_ROW_SYMPTOM_NAME + ", " +
                TABLE_ROW_PAIN_LEVEL + ", " +
                TABLE_ROW_NOTES + ", " +
                TABLE_ROW_TIMESTAMP +
                ") " +
                "VALUES (" +
                "'" + symptom.getBodyPart() + "', " +
                "'" + symptom.getSymptomName() + "', " +
                symptom.getPainLevel() + ", " +
                "'" + symptom.getNotes() + "', '" +
                symptom.getTimestamp() +
                "');";

        Log.i("insert() = ", query);

        // Best to use try catch ( but left oout for simplicity)
        db.execSQL(query);
    }

    // Delete a symptom record
    public void delete(int symptomId) {
        String query = "DELETE FROM " + TABLE_SYMPTOMS +
                " WHERE " + TABLE_ROW_ID + " = " + symptomId;

        Log.i("delete() = ", query);
        db.execSQL(query); // Deletes the records by the symptom id
    }

    // Update a symptom record
    public void update(Symptom symptom) {
        String query = "UPDATE " + TABLE_SYMPTOMS + " SET " +
                TABLE_ROW_BODY_PART + " = '" + symptom.getBodyPart() + "', " +
                TABLE_ROW_SYMPTOM_NAME + " = '" + symptom.getSymptomName() + "', " +
                TABLE_ROW_PAIN_LEVEL + " = " + symptom.getPainLevel() + ", " +
                TABLE_ROW_NOTES + " = '" + symptom.getNotes() + "', " +
                TABLE_ROW_TIMESTAMP + " = '" + symptom.getTimestamp() +
                "' WHERE " + TABLE_ROW_ID + " = " + symptom.getId();

        Log.i("update() = ", query);
        db.execSQL(query);
    }


    // Get all symptom records
    public List<Symptom> selectAll() {
        List<Symptom> symptomList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_SYMPTOMS;
        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(TABLE_ROW_ID));
            String bodyPart = cursor.getString(cursor.getColumnIndexOrThrow(TABLE_ROW_BODY_PART));
            String symptomName = cursor.getString(cursor.getColumnIndexOrThrow(TABLE_ROW_SYMPTOM_NAME));
            int painLevel = cursor.getInt(cursor.getColumnIndexOrThrow(TABLE_ROW_PAIN_LEVEL));
            String notes = cursor.getString(cursor.getColumnIndexOrThrow(TABLE_ROW_NOTES));
            String timestamp = cursor.getString(cursor.getColumnIndexOrThrow(TABLE_ROW_TIMESTAMP));

            Symptom symptom = new Symptom(bodyPart, symptomName, painLevel, notes, timestamp);
            symptom.setId(id); // Use the setter method
            symptomList.add(symptom);
        }

        cursor.close();
        return symptomList;
    }








}
