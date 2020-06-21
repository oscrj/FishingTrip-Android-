package com.example.fishingtrip.databas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;
import com.example.fishingtrip.models.AppUser;
import com.example.fishingtrip.models.FishingTrip;

import java.util.ArrayList;
import java.util.List;

import static com.example.fishingtrip.constants.QueryMessages.APP_USER_TABLE;
import static com.example.fishingtrip.constants.QueryMessages.COL_APP_USER;
import static com.example.fishingtrip.constants.QueryMessages.COL_EMAIL;
import static com.example.fishingtrip.constants.QueryMessages.COL_FIRST_NAME;
import static com.example.fishingtrip.constants.QueryMessages.COL_FISHING_METHOD;
import static com.example.fishingtrip.constants.QueryMessages.COL_FISHING_TRIP_ID;
import static com.example.fishingtrip.constants.QueryMessages.COL_IS_ACTIVE;
import static com.example.fishingtrip.constants.QueryMessages.COL_LAST_NAME;
import static com.example.fishingtrip.constants.QueryMessages.COL_LOCATION;
import static com.example.fishingtrip.constants.QueryMessages.COL_PASSWORD;
import static com.example.fishingtrip.constants.QueryMessages.COL_USER_ID;
import static com.example.fishingtrip.constants.QueryMessages.COL_USER_NAME;
import static com.example.fishingtrip.constants.QueryMessages.COL_WATER_TYPE;
import static com.example.fishingtrip.constants.QueryMessages.FISHING_TRIP_TABLE;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(@Nullable Context context) {
        super(context, "fishingTrip.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //  Create APP_USER_TABLE in database.
        String createAppUserTable = "CREATE TABLE " + APP_USER_TABLE + " (" + COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_USER_NAME + " TEXT NOT NULL UNIQUE, "
                + COL_PASSWORD + " TEXT, " + COL_FIRST_NAME + " TEXT, " + COL_LAST_NAME + " TEXT, " + COL_EMAIL + " TEXT)";
        db.execSQL(createAppUserTable);

        // Create FISHING_TRIP_TABLE
        String createFishingTrip = "CREATE TABLE " + FISHING_TRIP_TABLE + " (" + COL_FISHING_TRIP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_FISHING_METHOD + " TEXT, "
                + COL_WATER_TYPE + " TEXT, " + COL_LOCATION + " TEXT, " + COL_APP_USER + " TEXT, " + COL_IS_ACTIVE +  " BOOL)";
        db.execSQL(createFishingTrip);

        // Create CATCHES_TABLE
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
     * Get all users registered in database.
     * @return - Return a list of all AppUsers
     */
    public List<AppUser> getAllUsers(){
        SQLiteDatabase db = getReadableDatabase();
        List<AppUser> userList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + APP_USER_TABLE, null);

        if (cursor.moveToFirst()){
            do {
                AppUser tempUser = new AppUser(cursor.getInt(cursor.getColumnIndex(COL_USER_ID)),
                        cursor.getString(cursor.getColumnIndex(COL_USER_NAME)),
                        cursor.getString(cursor.getColumnIndex(COL_PASSWORD)),
                        cursor.getString(cursor.getColumnIndex(COL_FIRST_NAME)),
                        cursor.getString(cursor.getColumnIndex(COL_LAST_NAME)),
                        cursor.getString(cursor.getColumnIndex(COL_EMAIL)));
                userList.add(tempUser);

            }while (cursor.moveToNext());
        }else{
            // throw new IllegalArgumentException("Users Not Found!");
        }
        db.close();
        cursor.close();
        return userList;
    }
    /**
     * Find user by specific userName.
     * @param userName - Find AppUser in database that match userName.
     * @return Will return the AppUser with matching userName.
     */
    public AppUser findAppUserByUserName(String userName) {
        AppUser tempAppUser = new AppUser(-1, null, null, null,null, null);
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " +APP_USER_TABLE+ " WHERE " +COL_USER_NAME+ " = " + "'" + userName + "'", null);

        if (cursor.moveToFirst()){
            tempAppUser = new AppUser(cursor.getInt(cursor.getColumnIndex(COL_USER_ID)), cursor.getString(cursor.getColumnIndex(COL_USER_NAME)),
                    cursor.getString(cursor.getColumnIndex(COL_PASSWORD)), cursor.getString(cursor.getColumnIndex(COL_FIRST_NAME)),
                    cursor.getString(cursor.getColumnIndex(COL_LAST_NAME)), cursor.getString(cursor.getColumnIndex(COL_EMAIL)));
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
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + APP_USER_TABLE + " WHERE " + COL_USER_NAME + " = " + "'" + userName + "'" + " AND "
                + COL_PASSWORD + " = " + "'" + password + "'", null);
        int cursorCount = cursor.getCount();
        if (cursorCount > 0) {
            cursor.close();
            db.close();
            return true;
        }else{
            cursor.close();
            db.close();
            return false;
        }
    }

    /**
     * Method to update appUser.
     * @param user - user that will be updated.
     */
    public void updateUser(AppUser user){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE " + APP_USER_TABLE + " SET " + COL_USER_NAME + " = " + "'" + user.getUserName() + "'" + ", " + COL_PASSWORD + " = "
                + "'" + user.getPassword() + "'" + " , " + COL_FIRST_NAME + " = " + "'" + user.getFirstName() + "'" + " , " + COL_LAST_NAME + " = "
                + "'" + user.getLastName() + "'" + " , " + COL_EMAIL + " = " + "'" + user.getEmail() + "'"
                + " WHERE " + COL_USER_ID + " = " + "'" + user.getUserId() + "'");
        db.close();
    }

    /**
     * Method to remove appUser
     * @param user - user that will be removed.
     * @return - true or false if user was removed or not.
     */
    public boolean deleteUser(AppUser user){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("DELETE FROM " + APP_USER_TABLE + " WHERE " + COL_USER_ID + " = " + user.getUserId(), null);
        if (cursor.moveToFirst()){
            db.close();
            cursor.close();
            return false;
        }else {
            db.close();
            cursor.close();
            return true;
        }
    }

    /**
     * Add new fishing trip to database
     * @param fishingTrip - created fishingTrip to be added to database.
     * @return return true if fishingTrip was successfully inserted to database. If not, false is return to indicate that fishingTrip
     * was not inserted to database.
     */
    public boolean addFishingTrip(FishingTrip fishingTrip){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_FISHING_METHOD, fishingTrip.getFishingMethod());
        contentValues.put(COL_WATER_TYPE, fishingTrip.getWaterType());
        contentValues.put(COL_LOCATION, fishingTrip.getLocation());
        contentValues.put(COL_APP_USER, fishingTrip.getAppUser());
        contentValues.put(COL_IS_ACTIVE, fishingTrip.isActive());

        long insertStatus = db.insert(FISHING_TRIP_TABLE, null, contentValues);
        if(insertStatus == -1){
            db.close();
            return false;
        }else{
            db.close();
            return true;
        }
    }

    /**
     * Get all fishingtrips that is connected to a specific user.
     * @param userName - username that binds fisher to specific trip
     * @return will return a List of fishingtrip made by user with userName.
     */
    public List<FishingTrip> getAllFishingTripByUserName(String userName){
        SQLiteDatabase db = getReadableDatabase();
        List<FishingTrip> fishingTrips = new ArrayList<>();
        String getAllFishingTripByUserName = "SELECT * FROM " + FISHING_TRIP_TABLE + " WHERE " + COL_APP_USER + " = " + "'" + userName + "'";
        Cursor cursor = db.rawQuery(getAllFishingTripByUserName, null);

        if (cursor.moveToFirst()){
            do{
                boolean isActive = cursor.getInt(5) == 1 ? true:false;
                FishingTrip tempFishingTrip = new FishingTrip(cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        isActive);

                fishingTrips.add(tempFishingTrip);

            }while (cursor.moveToNext());

        }else {
            //throw new IllegalArgumentException("Didn't find any Trips!! ");
        }

        db.close();
        cursor.close();
        return fishingTrips;
    }

    /**
     * Update fishing trip
     * @param trip - fishing trip that will be updated.
     */
    public void updateTrip(FishingTrip trip){
        SQLiteDatabase db = getWritableDatabase();
        int isActive = trip.isActive() == true ? 1:0;
        db.execSQL("UPDATE " + FISHING_TRIP_TABLE + " SET " + COL_FISHING_METHOD + " = " + "'" + trip.getFishingMethod() + "'" + ", "
                + COL_WATER_TYPE + " = " + "'" + trip.getWaterType() + "'" + " , "
                + COL_LOCATION + " = " + "'" + trip.getLocation() + "'" + " , "
                + COL_APP_USER + " = " + "'" + trip.getAppUser() + "'" + " , "
                + COL_IS_ACTIVE + " = " + "'" + isActive + "'"
                + " WHERE " + COL_FISHING_TRIP_ID + " = " + "'" + trip.getFishingTripId() + "'");
        db.close();
    }

    /**
     * Delete fishing trip
     * @param trip - trip that will be deleted
     * @return true or false if trip was or was not removed from database.
     */
    public boolean deleteFishingTrip(FishingTrip trip){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("DELETE FROM " + FISHING_TRIP_TABLE + " WHERE " + COL_FISHING_TRIP_ID + " = " + trip.getFishingTripId(), null);
        if (cursor.moveToFirst()){
            db.close();
            cursor.close();
            return false;
        }else{
            db.close();
            cursor.close();
            return true;
        }
    }
}
