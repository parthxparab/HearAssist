package com.example.parthxparab.hearassist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

/**
 * Created by User on 2/28/2017.
 */

public class DbHelper extends SQLiteOpenHelper {

    private static final String TAG = "DbHelper";

    private static final String TABLE_NAME = "image_db";
    public static final String DATABASE_NAME = "audiogram.db";
    private static  int COL0 = 0;
    private static final String COL1 = "name";
    public static final String COL2 = "path";
    public static final String COL3 = "user";
    public static final String COL4 = "age";


    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable =  "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " NAME TEXT, PATH TEXT, USER TEXT, AGE TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(String name, String path, String user, String age) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, name);
        contentValues.put(COL2,path);
        contentValues.put(COL3,user);
        contentValues.put(COL4,age);

        Log.d(TAG, "addData: Adding " + name +"AND"+ path + " to " + TABLE_NAME);

        long result = db.insert(TABLE_NAME, null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Returns all the data from database
     * @return
     */
    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }




    /**
     * Returns only the ID that matches the name passed in
     * @param name
     * @return
     */
    public Cursor getItemID(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + COL0 + " FROM " + TABLE_NAME +
                " WHERE " + COL1 + " = '" + name + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    /**
     * Updates the name field
     * @param newName
     * @param id
     * @param oldName
     */

    /**
     * Delete from database
     */
    public void deleteData(String name){

        SQLiteDatabase db = getWritableDatabase();

        String sql = "DELETE FROM " + TABLE_NAME + " WHERE "
                + COL1 + " = " + name ;
        SQLiteStatement statement = db.compileStatement(sql);
//        statement.clearBindings();
//        statement.bindDouble(1, (double)id);

        statement.execute();
        db.close();
    }

    public String addUser(String name){
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT " + COL3 + " FROM " + TABLE_NAME +
                " WHERE " + COL1 + " = '" + name + "'";
        SQLiteStatement statement = db.compileStatement(query);
        statement.execute();
        db.close();

        return query;
    }

}

