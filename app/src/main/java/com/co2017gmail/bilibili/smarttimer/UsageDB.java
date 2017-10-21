package com.co2017gmail.bilibili.smarttimer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bilibili on 2017/10/12.
 */

public class UsageDB {
    /**
     * find all data
     * @param context
     * @return
     */
    public static List<Usage> findAll(Context context) {
        // TODO Auto-generated method stub
        SQLiteDatabase db = null;
        List<Usage>usages =new ArrayList<Usage>();
        try {
            db = getDataBase(context);
            Usage usage = null;
            Cursor c = db.query(SqliteDBField.Usage.TABLE,null,null,null,null,null,null);
            while(c.moveToNext()){
                usage = new Usage();
                usage.dateTime = c.getString(c.getColumnIndex(SqliteDBField.Usage.DATETIME));
                usage.totalUsage = c.getLong(c.getColumnIndex(SqliteDBField.Usage.TOTALUSAGE));
                usage.wastedTime = c.getLong(c.getColumnIndex(SqliteDBField.Usage.WASTEDTIME));
                usage.score = c.getInt(c.getColumnIndex(SqliteDBField.Usage.SCORE));
                usages.add(usage);
            }
            c.close();
            db.close();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.err.println(e);
        }

        return usages;
    }
    public static void insert(Context context,Usage usage){
        SQLiteDatabase db;
        try {
            db = getDataBase(context);
            ContentValues cv = new ContentValues();
            cv.put(SqliteDBField.Usage.DATETIME,usage.dateTime);
            cv.put(SqliteDBField.Usage.TOTALUSAGE,usage.totalUsage);
            cv.put(SqliteDBField.Usage.WASTEDTIME,usage.wastedTime);
            cv.put(SqliteDBField.Usage.SCORE,usage.score);
            db.insert(SqliteDBField.Usage.TABLE,null,cv);
            db.close();
        }catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.err.println(e.toString());
        }
    }
    public static int delete(Context context,String datetTime){
        SQLiteDatabase db = null;
        try {
            db = getDataBase(context);
            int result = db.delete(SqliteDBField.Usage.TABLE, SqliteDBField.Usage.DATETIME+"=?",new String[]{datetTime});
            return result;
        }catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.err.println(e.toString());
        }
        return -1;
    }
    public static int update(Context context,Usage usage){

        SQLiteDatabase db = null;
        try {
            db = getDataBase(context);
            ContentValues cv = new ContentValues();
            cv.put(SqliteDBField.Usage.TOTALUSAGE,usage.totalUsage);
            cv.put(SqliteDBField.Usage.WASTEDTIME,usage.wastedTime);
            cv.put(SqliteDBField.Usage.SCORE,usage.score);
            int result = db.update(SqliteDBField.Usage.TABLE, cv, SqliteDBField.Usage.DATETIME+"=?",new String[]{usage.dateTime});
            return result;

        }catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.err.println(e.toString());
        }
        return -1;
    }
    public static Usage find(Context context,String datetTime) {
        // TODO Auto-generated method stub
        SQLiteDatabase db = null;
        Usage usage =null;
        try {
            db = getDataBase(context);
            Cursor c = db.rawQuery("select * from "+SqliteDBField.Usage.TABLE+" where "+SqliteDBField.Usage.DATETIME+"=?",new String[]{datetTime});
            if(c.moveToFirst()){//determine wether the cursor is null
                usage = new Usage();
                usage.dateTime = c.getString(c.getColumnIndex(SqliteDBField.Usage.DATETIME));
                usage.totalUsage = c.getLong(c.getColumnIndex(SqliteDBField.Usage.TOTALUSAGE));
                usage.wastedTime = c.getLong(c.getColumnIndex(SqliteDBField.Usage.WASTEDTIME));
                usage.score = c.getInt(c.getColumnIndex(SqliteDBField.Usage.SCORE));
            }
            c.close();
            db.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.err.println(e);
        }
        return usage;
    }
    public static SQLiteDatabase getDataBase(Context context) throws IOException {
        SQLiteDatabase db = new SmartTimerSqliteOpenHelper(context).getWritableDatabase();
        return db;
    }
}
