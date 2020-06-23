package com.example.fishingtrip.databas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;
import com.example.fishingtrip.models.AppUser;
import com.example.fishingtrip.models.Catch;
import com.example.fishingtrip.models.FishingTrip;

import java.util.ArrayList;
import java.util.List;

import static com.example.fishingtrip.constants.QueryMessages.APP_USER_TABLE;
import static com.example.fishingtrip.constants.QueryMessages.COL_APP_USER;
import static com.example.fishingtrip.constants.QueryMessages.COL_CATCH_FISHING_TRIP_ID;
import static com.example.fishingtrip.constants.QueryMessages.COL_EMAIL;
import static com.example.fishingtrip.constants.QueryMessages.COL_FIRST_NAME;
import static com.example.fishingtrip.constants.QueryMessages.COL_FISHING_METHOD;
import static com.example.fishingtrip.constants.QueryMessages.COL_FISHING_TRIP_ID;
import static com.example.fishingtrip.constants.QueryMessages.COL_FISH_CATCH_ID;
import static com.example.fishingtrip.constants.QueryMessages.COL_IS_ACTIVE;
import static com.example.fishingtrip.constants.QueryMessages.COL_LAST_NAME;
import static com.example.fishingtrip.constants.QueryMessages.COL_LENGTH;
import static com.example.fishingtrip.constants.QueryMessages.COL_LOCATION;
import static com.example.fishingtrip.constants.QueryMessages.COL_PASSWORD;
import static com.example.fishingtrip.constants.QueryMessages.COL_SPECIES;
import static com.example.fishingtrip.constants.QueryMessages.COL_USER_ID;
import static com.example.fishingtrip.constants.QueryMessages.COL_USER_NAME;
import static com.example.fishingtrip.constants.QueryMessages.COL_WATER_TYPE;
import static com.example.fishingtrip.constants.QueryMessages.COL_WEIGHT;
import static com.example.fishingtrip.constants.QueryMessages.FISHING_TRIP_TABLE;
import static com.example.fishingtrip.constants.QueryMessages.FISH_CAUGHT_TABLE;

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
        String createCatchTable = "CREATE TABLE " + FISH_CAUGHT_TABLE + " (" + COL_FISH_CATCH_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_SPECIES + " TEXT, "
                + COL_LENGTH + " REAL, " + COL_WEIGHT + " REAL, " + COL_CATCH_FISHING_TRIP_ID + " TEXT)";
        db.execSQL(createCatchTable);
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
        db.close();
        return tempAppUser;
    }

    /**
     * This method check if user exist or not. userName is unique in database and cant be two users with the same userName.
     * @param userName - check if there is a user with this userName that also has the exact password.
     * @param password - check if there is a user with this password that also has the exact userName.
     * @return true/false - if both condition is true / if none or only one of the conditions is true
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
     * Get fishingTrip from database with specific ID
     * @param fishingTripId - get Trip with specific id.
     * @return - the trip matching the specific id.
     */
    public FishingTrip getFishingTripById(String fishingTripId) {
        SQLiteDatabase db = getReadableDatabase();
        FishingTrip tempTrip = null;
        String getAllFishingTripByUserName = "SELECT * FROM " + FISHING_TRIP_TABLE + " WHERE " + COL_FISHING_TRIP_ID + " = " + "'" + fishingTripId + "'";
        Cursor cursor = db.rawQuery(getAllFishingTripByUserName, null);

        if (cursor.moveToFirst()){
            do{
                boolean isActive = cursor.getInt(5) == 1 ? true:false;
                tempTrip = new FishingTrip(cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        isActive);


            }while (cursor.moveToNext());

        }else {
            //throw new IllegalArgumentException("Didn't find any Trips!! ");
        }

        db.close();
        cursor.close();
        return tempTrip;
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
     * Check if there is a active FishingTrip that user whit userName has created.
     * @param userName - Check only trips that is bind to user with this userName.
     * @return true/false - if there is any active trips or not made by user with userName.
     */
    public boolean checkForActiveTrips(String userName) {
        SQLiteDatabase db = this.getReadableDatabase();
        int isActive = 1;
                Cursor cursor = db.rawQuery("SELECT * FROM " + FISHING_TRIP_TABLE + " WHERE " + COL_APP_USER + " = " + "'" + userName + "'" + " AND "
                + COL_IS_ACTIVE + " = " + "'" + isActive + "'", null);
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
     * Get trip that is active.
     * @param userName - user that created trip.
     * @return - trip that is active and created by user with username.
     */
    public FishingTrip getActiveTrip(String userName){
        SQLiteDatabase db = getReadableDatabase();
        FishingTrip tempTrip = null;
        int active = 1;
        Cursor cursor = db.rawQuery("SELECT * FROM " + FISHING_TRIP_TABLE + " WHERE " + COL_APP_USER + " = " + "'" + userName + "'" + " AND "
                + COL_IS_ACTIVE + " = " + "'" + active + "'", null);

        if (cursor.moveToFirst()){
            do{
                boolean isActive = cursor.getInt(5) == 1 ? true:false;
                tempTrip = new FishingTrip(cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        isActive);
            }while (cursor.moveToNext());

        }else {
            //throw new IllegalArgumentException("Didn't find any Trips!! ");
        }
        db.close();
        cursor.close();
        return tempTrip;
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

    /**
     * Add caught fish to database
     * @param fishCaught - fish that is caught.
     * @return - true or false if catch was or was not added to database.
     */
    public boolean addCatch(Catch fishCaught){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COL_SPECIES, fishCaught.getSpecies());
        cv.put(COL_LENGTH, fishCaught.getLength());
        cv.put(COL_WEIGHT, fishCaught.getWeight());
        cv.put(COL_CATCH_FISHING_TRIP_ID, fishCaught.getFishingTrip());

        long insertStatus = db.insert(FISH_CAUGHT_TABLE, null, cv);
        if(insertStatus == -1){
            db.close();
            return false;
        }else{
            db.close();
            return true;
        }
    }

    /**
     * Find all caught fish on a specific trip.
     * @param fishingTripId - trip that catches are connected to.
     * @return - List of all fish caught on that trip.
     */
    public List<Catch> getCatchWithFishTripId(String fishingTripId) {
        SQLiteDatabase db = getReadableDatabase();
        List<Catch> catches = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM " + FISH_CAUGHT_TABLE + " WHERE " + COL_CATCH_FISHING_TRIP_ID + " = " + "'" + fishingTripId + "'", null);
        if (cursor.moveToFirst()){
            do {
                Catch tempCatch = new Catch(cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getDouble(2),
                        cursor.getDouble(3),
                        cursor.getString(4));

                catches.add(tempCatch);

            }while (cursor.moveToNext());

        }else{
            // Handle exceptions...
        }
        db.close();
        cursor.close();
        return catches;
    }

    /**
     * Delete caught fish.
     * @param fishCaught - Catch that will be deleted.
     * @return - true or false if catch was or was not removed from database.
     */
    public boolean deleteCatch(Catch fishCaught){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("DELETE FROM " + FISH_CAUGHT_TABLE + " WHERE " + COL_FISH_CATCH_ID + " = " + fishCaught.getCatchId(), null);
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
