package com.co2017gmail.bilibili.smarttimer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bilibili on 2017/10/10.
 */

public class UserDB {

    public static void insert(Context context,User user){
        SQLiteDatabase db;
        try {
            db = getDataBase(context);
            ContentValues cv = new ContentValues();
            cv.put(SqliteDBField.User.USERNAME,user.userName);
            cv.put(SqliteDBField.User.PASSWORD,user.password);
            cv.put(SqliteDBField.User.AVATOR,user.avator);
            cv.put(SqliteDBField.User.EMAIL,user.email);
            cv.put(SqliteDBField.User.PHONENUMBER,user.phoneNumber);
            cv.put(SqliteDBField.User.DAILYUSAGELIMIT,user.dailyUsageLimit);
            cv.put(SqliteDBField.User.NOTIFICATIONSWITCH,user.notificationSwitch?1:0);
            cv.put(SqliteDBField.User.RESTRICTIONSWITCH,user.restrictionSwitch?1:0);
            cv.put(SqliteDBField.User.WEDGETSWITCH,user.wedgetSwitch?1:0);
            db.insert(SqliteDBField.User.TABLE,null,cv);
            db.close();
        }catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.err.println(e.toString());
        }
    }
    public static int update(Context context,User user){

        SQLiteDatabase db = null;
        try {
            db = getDataBase(context);
            ContentValues cv = new ContentValues();
            cv.put(SqliteDBField.User.PASSWORD,user.password);
            cv.put(SqliteDBField.User.AVATOR,user.avator);
            cv.put(SqliteDBField.User.EMAIL,user.email);
            cv.put(SqliteDBField.User.PHONENUMBER,user.phoneNumber);
            cv.put(SqliteDBField.User.DAILYUSAGELIMIT,user.dailyUsageLimit);
            cv.put(SqliteDBField.User.NOTIFICATIONSWITCH,user.notificationSwitch?1:0);
            cv.put(SqliteDBField.User.RESTRICTIONSWITCH,user.restrictionSwitch?1:0);
            cv.put(SqliteDBField.User.WEDGETSWITCH,user.wedgetSwitch?1:0);
            int result = db.update(SqliteDBField.User.TABLE, cv, SqliteDBField.User.USERNAME+"=?",new String[]{user.userName});
            return result;

        }catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.err.println(e.toString());
        }
        return -1;
    }
    public static User find(Context context,String name) {
        // TODO Auto-generated method stub
        SQLiteDatabase db = null;
        User user =null;
        try {
            db = getDataBase(context);
            Cursor c = db.rawQuery("select * from "+SqliteDBField.User.TABLE+" where "+SqliteDBField.User.USERNAME+"=?",new String[]{name});
            if(c.moveToFirst()){//determine wether the cursor is null
                user = new User();
                user.userName = c.getString(c.getColumnIndex(SqliteDBField.User.USERNAME));
                user.password = c.getString(c.getColumnIndex(SqliteDBField.User.PASSWORD));
                user.avator = c.getString(c.getColumnIndex(SqliteDBField.User.AVATOR));
                user.email = c.getString(c.getColumnIndex(SqliteDBField.User.EMAIL));
                user.phoneNumber = c.getString(c.getColumnIndex(SqliteDBField.User.PHONENUMBER));
                user.dailyUsageLimit = c.getInt(c.getColumnIndex(SqliteDBField.User.DAILYUSAGELIMIT));
                user.notificationSwitch = c.getInt(c.getColumnIndex(SqliteDBField.User.NOTIFICATIONSWITCH))==1;
                user.restrictionSwitch = c.getInt(c.getColumnIndex(SqliteDBField.User.RESTRICTIONSWITCH))==1;
                user.wedgetSwitch = c.getInt(c.getColumnIndex(SqliteDBField.User.WEDGETSWITCH))==1;
            }
            c.close();
            db.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.err.println(e);
        }
        return user;
    }
    public static SQLiteDatabase getDataBase(Context context) throws IOException {
        SQLiteDatabase db = new SmartTimerSqliteOpenHelper(context).getWritableDatabase();
        return db;
    }
}
