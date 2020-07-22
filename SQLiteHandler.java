package com.ione.iseller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;

/**
 * Author: Shiv Bhushan Tripathi.
 * Date Started: 26/ 03/ 2017.
 * Description: Class that handle welcome and login Screen.
 * @copyright iOne: A company of Ikai.
 */

class SQLiteHandler extends SQLiteOpenHelper{

    // Name of database version.
    private static final int DB_VERSION = 1;
    // Name of database.
    private static final String DB_NAME = "iSeller";
    // Name of table.
    private static final String TABLE_NAME = "seller";

    // Column names of seller table
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_MOBILE = "mobile";
    private static final String KEY_UID = "uid";

    SQLiteHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE" + TABLE_NAME + "(" + KEY_ID + " INTEGER " +
                "PRIMARY KEY AUTOINCREMENT, " + KEY_NAME + " TEXT NOT NULL, " + KEY_EMAIL + " TEXT UNIQUE, " +
                KEY_MOBILE + " TEXT UNIQUE, " + KEY_UID + " TEXT" + ")";
        db.execSQL(CREATE_LOGIN_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    void addUser(String name, String email, String mobile, String uid) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name); // Name
        values.put(KEY_EMAIL, email); // Email
        values.put(KEY_MOBILE, mobile); // Mobile number
        values.put(KEY_UID, uid); // Uid

        // Inserting Row
        long id = db.insert(TABLE_NAME, null, values);
        db.close(); // Closing database connection

    }

    HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("name", cursor.getString(1));
            user.put("email", cursor.getString(2));
            user.put("mobile", cursor.getString(3));
            user.put("uid", cursor.getString(4));
        }
        cursor.close();
        db.close();

        return user;
    }

    void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_NAME, null, null);
        db.close();
    }
}
