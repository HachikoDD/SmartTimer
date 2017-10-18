package com.co2017gmail.bilibili.smarttimer;

import android.app.usage.UsageEvents;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bilibili on 2017/10/11.
 */

public class EventsDB {
    /**
     * find all data
     * @param context
     * @return
     */
    public static List<Events> findAll(Context context) {
        // TODO Auto-generated method stub
        SQLiteDatabase db = null;
        List<Events>events =new ArrayList<Events>();
        try {
            db = getDataBase(context);
            Events event = null;
            Cursor c = db.query(SqliteDBField.Events.TABLE,null,null,null,null,null,null);
            while(c.moveToNext()){
                event = new Events();
                event.eventName = c.getString(c.getColumnIndex(SqliteDBField.Events.EVENTNAME));
                event.eventFinishTime = c.getString(c.getColumnIndex(SqliteDBField.Events.EVENTFINISHTIME));
                event.eventStartTime = c.getString(c.getColumnIndex(SqliteDBField.Events.EVENTSTARTTIME));
                event.eventStatusTime = c.getString(c.getColumnIndex(SqliteDBField.Events.EVENTSTATUSTIME));
                events.add(event);
            }
            c.close();
            db.close();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.err.println(e);
        }

        return events;
    }
    public static void insert(Context context,Events events){
        SQLiteDatabase db;
        try {
            db = getDataBase(context);
            ContentValues cv = new ContentValues();
            cv.put(SqliteDBField.Events.EVENTNAME,events.eventName);
            cv.put(SqliteDBField.Events.EVENTFINISHTIME,events.eventFinishTime);
            cv.put(SqliteDBField.Events.EVENTSTARTTIME,events.eventStartTime);
            cv.put(SqliteDBField.Events.EVENTSTATUSTIME,events.eventStatusTime);
            db.insert(SqliteDBField.Events.TABLE,null,cv);
            db.close();
        }catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.err.println(e.toString());
        }
    }
    public static int delete(Context context,String eventName){
        SQLiteDatabase db = null;
        try {
            db = getDataBase(context);
            int result = db.delete(SqliteDBField.Events.TABLE, SqliteDBField.Events.EVENTNAME+"=?",new String[]{eventName});
            return result;
        }catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.err.println(e.toString());
        }
        return -1;
    }
    public static int update(Context context,Events events){

        SQLiteDatabase db = null;
        try {
            db = getDataBase(context);
            ContentValues cv = new ContentValues();
            cv.put(SqliteDBField.Events.EVENTFINISHTIME,events.eventFinishTime);
            cv.put(SqliteDBField.Events.EVENTSTARTTIME,events.eventStartTime);
            cv.put(SqliteDBField.Events.EVENTSTATUSTIME,events.eventStatusTime);
            int result = db.update(SqliteDBField.Events.TABLE, cv, SqliteDBField.Usage.DATETIME+"=?",new String[]{events.eventName});
            return result;

        }catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.err.println(e.toString());
        }
        return -1;
    }
    public static Events find(Context context,String eventName) {
        // TODO Auto-generated method stub
        SQLiteDatabase db = null;
        Events event =null;
        try {
            db = getDataBase(context);
            Cursor c = db.rawQuery("select * from "+SqliteDBField.Events.TABLE+" where "+SqliteDBField.Events.EVENTNAME+"=?",new String[]{eventName});
            if(c.moveToFirst()){//determine wether the cursor is null
                event = new Events();
                event.eventName = c.getString(c.getColumnIndex(SqliteDBField.Events.EVENTNAME));
                event.eventFinishTime = c.getString(c.getColumnIndex(SqliteDBField.Events.EVENTFINISHTIME));
                event.eventStartTime = c.getString(c.getColumnIndex(SqliteDBField.Events.EVENTSTARTTIME));
                event.eventStatusTime = c.getString(c.getColumnIndex(SqliteDBField.Events.EVENTSTATUSTIME));
            }
            c.close();
            db.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.err.println(e);
        }
        return event;
    }
    public static SQLiteDatabase getDataBase(Context context) throws IOException {
        SQLiteDatabase db = new SmartTimerSqliteOpenHelper(context).getWritableDatabase();
        return db;
    }
}
