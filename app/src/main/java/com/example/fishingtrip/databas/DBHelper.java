package com.example.fishingtrip.databas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;
import com.example.fishingtrip.models.AppUser;

import static com.example.fishingtrip.constants.QueryMessages.APP_USER_TABLE;
import static com.example.fishingtrip.constants.QueryMessages.COL_EMAIL;
import static com.example.fishingtrip.constants.QueryMessages.COL_FIRST_NAME;
import static com.example.fishingtrip.constants.QueryMessages.COL_LAST_NAME;
import static com.example.fishingtrip.constants.QueryMessages.COL_PASSWORD;
import static com.example.fishingtrip.constants.QueryMessages.COL_USER_ID;
import static com.example.fishingtrip.constants.QueryMessages.COL_USER_NAME;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(@Nullable Context context) {
        super(context, "fishingTrip.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //  Create APP_USER_TABLE in database.
        String createAppUserTable = "CREATE TABLE " + APP_USER_TABLE + " (" + COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_USER_NAME + " TEXT, "
                + COL_PASSWORD + " TEXT, " + COL_FIRST_NAME + " TEXT, " + COL_LAST_NAME + " TEXT, " + COL_EMAIL + " TEXT)";
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
        db.close();
        if (insertStatus == -1){
            return false;
        }else{
            return true;
        }
    }

    /**
     * Find user by specific userName.
     * @param userName - Find AppUser in database that match userName.
     * @return Will return the AppUser with matching userName.
     */
    public AppUser findAppUserByUserName(String userName) throws IllegalArgumentException{
        AppUser tempAppUser = null;

        SQLiteDatabase db = getReadableDatabase();

        String getUserWithUsername = "SELECT * FROM " +APP_USER_TABLE+ " WHERE " +COL_USER_NAME+ " = " + userName;
        Cursor cursor = db.rawQuery(getUserWithUsername, null);

        if (cursor.moveToFirst()){
            tempAppUser = new AppUser(cursor.getInt(cursor.getColumnIndex(COL_USER_ID)), cursor.getString(cursor.getColumnIndex(COL_USER_NAME)),
                    cursor.getString(cursor.getColumnIndex(COL_PASSWORD)), cursor.getString(cursor.getColumnIndex(COL_FIRST_NAME)),
                    cursor.getString(cursor.getColumnIndex(COL_LAST_NAME)), cursor.getString(cursor.getColumnIndex(COL_EMAIL)));
        }else{
            throw new IllegalArgumentException("User not found!");
        }
        cursor.close();
        return tempAppUser;
    }

    /**
     * This method to check user exist or not
     *
     * @param userName
     * @param password
     * @return true/false
     */
    public boolean checkUser(String userName, String password) {
        //  array of columns to fetch
        String[] columns = {
                COL_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        //  selection criteria
        String selection = COL_USER_NAME + " = ?" + " AND " + COL_PASSWORD + " = ?";
        //  selection arguments
        String[] selectionArgs = {userName, password};
        //  query user table with conditions
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com' AND user_password = 'qwerty';
         */
        Cursor cursor = db.query(APP_USER_TABLE, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order
        /*
        String checkUserNameAndPassword = "SELECT * FROM " +APP_USER_TABLE+ " WHERE " +COL_USER_NAME+ " = " + userName + " AND "
                + COL_PASSWORD + " = " + password;
        Cursor cursor = db.rawQuery(checkUserNameAndPassword, null);
        */
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }else{
            return false;
        }
    }
}
