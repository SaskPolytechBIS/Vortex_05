package com.example.mscarenew.ui.symptoms;

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

    // Column names
    public static final String TABLE_ROW_ID = "_id";
    public static final String TABLE_ROW_BODY_PART = "bodyPart";
    public static final String TABLE_ROW_SYMPTOM_NAME = "symptomName";
    public static final String TABLE_ROW_PAIN_LEVEL = "painLevel";
    public static final String TABLE_ROW_NOTES = "notes";
    public static final String TABLE_ROW_TIMESTAMP = "timestamp";

    private static final String DB_NAME = "symptoms_db";
    private static final int DB_VERSION = 1;
    private static final String TABLE_SYMPTOMS = "symptoms";

    public SymptomDbHelper(Context context) {
        CustomSQLiteOpenHelper helper = new CustomSQLiteOpenHelper(context);
        db = helper.getWritableDatabase();
    }

    private class CustomSQLiteOpenHelper extends SQLiteOpenHelper {
        public CustomSQLiteOpenHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String newTableQueryString = "CREATE TABLE " + TABLE_SYMPTOMS + " ("
                    + TABLE_ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                    + TABLE_ROW_BODY_PART + " TEXT NOT NULL, "
                    + TABLE_ROW_SYMPTOM_NAME + " TEXT NOT NULL, "
                    + TABLE_ROW_PAIN_LEVEL + " INTEGER NOT NULL, "
                    + TABLE_ROW_NOTES + " TEXT, "
                    + TABLE_ROW_TIMESTAMP + " TEXT NOT NULL);";
            db.execSQL(newTableQueryString);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // No upgrade logic needed
        }
    }

    // Insert a symptom record
    public void insert(Symptom symptom) {
        String query = "INSERT INTO " + TABLE_SYMPTOMS + " (" +
                TABLE_ROW_BODY_PART + ", " +
                TABLE_ROW_SYMPTOM_NAME + ", " +
                TABLE_ROW_PAIN_LEVEL + ", " +
                TABLE_ROW_NOTES + ", " +
                TABLE_ROW_TIMESTAMP +
                ") VALUES (" +
                "'" + symptom.getBodyPart() + "', " +
                "'" + symptom.getSymptomName() + "', " +
                symptom.getPainLevel() + ", " +
                "'" + symptom.getNotes() + "', '" +
                symptom.getTimestamp() +
                "');";
        Log.i("insert() = ", query);
        db.execSQL(query);
    }

    // Delete one record
    public void delete(int symptomId) {
        String query = "DELETE FROM " + TABLE_SYMPTOMS +
                " WHERE " + TABLE_ROW_ID + " = " + symptomId;
        Log.i("delete() = ", query);
        db.execSQL(query);
    }

    // Update a record
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

    // Select all records
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
            symptom.setId(id);
            symptomList.add(symptom);
        }

        cursor.close();
        return symptomList;
    }

    // ✅ NEW METHOD: Delete all records
    public void deleteAll() {
        String query = "DELETE FROM " + TABLE_SYMPTOMS;
        Log.i("deleteAll() = ", query);
        db.execSQL(query);
    }
}
