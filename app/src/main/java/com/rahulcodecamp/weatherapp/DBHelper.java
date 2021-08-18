package com.rahulcodecamp.weatherapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DBNAME = "Register.db";

    public DBHelper(Context context) {
        super(context,"Register.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        MyDB.execSQL("create Table users (phoneNo TEXT primary key, name TEXT, gender TEXT, age TEXT, address TEXT, district TEXT, pinCode TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int oldVersion, int newVersion) {
        MyDB.execSQL("drop table if exists users");
    }

    public Boolean insertData(String phoneNo, String name, String gender, int age, String address, String district, String pinCode){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("phoneNo", phoneNo);
        contentValues.put("name", name);
        contentValues.put("gender", gender);
        contentValues.put("age", age);
        contentValues.put("address", address);
        contentValues.put("district", district);
        contentValues.put("pinCode", pinCode);

        long result = MyDB.insert("users", null, contentValues);
        if (result == -1) return false;
        else return true;

    }

    public Boolean checkPhoneNo(String phoneNo){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("select * from users where phoneNo = ?", new String[] {phoneNo});
        if (cursor.getCount() > 0){
            return true;
        }
        else {
            return false;
        }
    }

    public Cursor getUserData(String phoneNo){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        String qry = "select * from users where phoneNo="+phoneNo;
        Cursor crs = MyDB.rawQuery(qry, null);
        return crs;
    }
}
