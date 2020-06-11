package com.example.fishingtrip.databas;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import static com.example.fishingtrip.constants.QueryMessages.APP_USER_TABLE;
import static com.example.fishingtrip.constants.QueryMessages.COL_EMAIL;
import static com.example.fishingtrip.constants.QueryMessages.COL_FIRST_NAME;
import static com.example.fishingtrip.constants.QueryMessages.COL_LAST_NAME;
import static com.example.fishingtrip.constants.QueryMessages.COL_PASSWORD;
import static com.example.fishingtrip.constants.QueryMessages.COL_REG_DATE;
import static com.example.fishingtrip.constants.QueryMessages.COL_USER_ID;
import static com.example.fishingtrip.constants.QueryMessages.COL_USER_NAME;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(@Nullable Context context) {
        super(context, "fishingTrip.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //  Create APP_USER_TABLE in database.
        String createAppUserTable = "CREATE TABLE " + APP_USER_TABLE + "( " + COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_USER_NAME + " TEXT, "
                + COL_PASSWORD + " TEXT, " + COL_FIRST_NAME + " TEXT, " + COL_LAST_NAME + " TEXT, " + COL_EMAIL + "TEXT" + COL_REG_DATE + " TEXT)";
        db.execSQL(createAppUserTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
