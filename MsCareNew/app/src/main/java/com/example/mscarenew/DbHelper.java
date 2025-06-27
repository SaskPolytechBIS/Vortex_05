package com.example.mscarenew;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME    = "mscare.db";
    private static final int    DB_VERSION = 2;

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
// CREATE users table
        String userSql = "CREATE TABLE " + DbContract.UserEntry.TABLE_NAME + " ("
                + DbContract.UserEntry.COL_ID      + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DbContract.UserEntry.COL_EMAIL   + " TEXT UNIQUE NOT NULL, "
                + DbContract.UserEntry.COL_PASSWORD+ " TEXT NOT NULL, "
                + DbContract.UserEntry.COL_CREATED + " TEXT NOT NULL"
                + ")";
        db.execSQL(userSql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // For now, simply drop & recreate
        db.execSQL("DROP TABLE IF EXISTS " + DbContract.UserEntry.TABLE_NAME);
        onCreate(db);
    }
}
