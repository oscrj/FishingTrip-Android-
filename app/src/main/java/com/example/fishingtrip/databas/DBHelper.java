package com.example.fishingtrip.databas;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.fishingtrip.models.AppUser;

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
        super(context, "fishing_trip.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //  Create APP_USER_TABLE in database.
        String createAppUserTable = "CREATE TABLE " + APP_USER_TABLE + " (" + COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_USER_NAME + " TEXT, "
                + COL_PASSWORD + " TEXT, " + COL_FIRST_NAME + " TEXT, " + COL_LAST_NAME + " TEXT, " + COL_EMAIL + "TEXT)";
        db.execSQL(createAppUserTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * Add new users to database.
     * @param appUser - Created user to be inserted(added) to database.
     * @return will return true if user was successfully inserted to database, if not false is return to indicate that user
     * was not inserted to database.
     */
    public boolean addAppUser(AppUser appUser){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_USER_NAME, appUser.getUserName());
        contentValues.put(COL_PASSWORD, appUser.getPassword());
        contentValues.put(COL_FIRST_NAME, appUser.getFirstName());
        contentValues.put(COL_LAST_NAME, appUser.getLastName());
        contentValues.put(COL_EMAIL, appUser.getEmail());

        long insertStatus = db.insert(APP_USER_TABLE, null, contentValues);

        if (insertStatus == -1){
            db.close();
            return false;
        }else{
            db.close();
            return true;
        }
    }

}
