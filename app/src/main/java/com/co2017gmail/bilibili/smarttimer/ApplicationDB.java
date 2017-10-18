package com.co2017gmail.bilibili.smarttimer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.co2017gmail.bilibili.smarttimer.SqliteDBField.*;

/**
 * Created by bilibili on 2017/10/13.
 */

public class ApplicationDB {
    /**
     * find all data
     * @param context
     * @return
     */
    public static List<App> findAll(Context context) {
        // TODO Auto-generated method stub
        SQLiteDatabase db = null;
        List<App>apps =new ArrayList<App>();
        try {
            db = getDataBase(context);
            App app = null;
            Cursor c = db.query(SqliteDBField.App.TABLE,null,null,null,null,null,null);
            while(c.moveToNext()){
                app = new App();
                app.name = c.getString(c.getColumnIndex(SqliteDBField.App.APPNAME));
                app.usage = c.getString(c.getColumnIndex(SqliteDBField.App.APPUSAGE));
                app.monitorSwitch = c.getInt(c.getColumnIndex(SqliteDBField.App.APPMONITORSWITCH))==1;
                apps.add(app);
            }
            c.close();
            db.close();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.err.println(e);
        }

        return apps;
    }
    public static void insert(Context context,App app){
        SQLiteDatabase db;
        try {
            db = getDataBase(context);
            ContentValues cv = new ContentValues();
            cv.put(SqliteDBField.App.APPNAME,app.name);
            cv.put(SqliteDBField.App.APPUSAGE,app.usage);
            cv.put(SqliteDBField.App.APPMONITORSWITCH,app.monitorSwitch?1:0);
            db.insert(SqliteDBField.App.TABLE,null,cv);
            db.close();
        }catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.err.println(e.toString());
        }
    }
    public static int delete(Context context,String name){
        SQLiteDatabase db = null;
        try {
            db = getDataBase(context);
            int result = db.delete(SqliteDBField.App.TABLE, SqliteDBField.App.APPNAME+"=?",new String[]{name});
            return result;
        }catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.err.println(e.toString());
        }
        return -1;
    }
    public static int update(Context context,App app){

        SQLiteDatabase db = null;
        try {
            db = getDataBase(context);
            ContentValues cv = new ContentValues();
            cv.put(SqliteDBField.App.APPUSAGE,app.usage);
            cv.put(SqliteDBField.App.APPMONITORSWITCH,app.monitorSwitch?1:0);
            int result = db.update(SqliteDBField.App.TABLE, cv, SqliteDBField.App.APPNAME+"=?",new String[]{app.name});
            return result;

        }catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.err.println(e.toString());
        }
        return -1;
    }
    public static App find(Context context,String name) {
        // TODO Auto-generated method stub
        SQLiteDatabase db = null;
        App app =null;
        try {
            db = getDataBase(context);
            Cursor c = db.rawQuery("select * from "+SqliteDBField.App.TABLE+" where "+SqliteDBField.App.APPNAME+"=?",new String[]{name});
            if(c.moveToFirst()){//determine wether the cursor is null
                app = new App();
                app.name = c.getString(c.getColumnIndex(SqliteDBField.App.APPNAME));
                app.usage = c.getString(c.getColumnIndex(SqliteDBField.App.APPUSAGE));
                app.monitorSwitch = c.getInt(c.getColumnIndex(SqliteDBField.App.APPMONITORSWITCH))==1;
            }
            c.close();
            db.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.err.println(e);
        }
        return app;
    }
    public static SQLiteDatabase getDataBase(Context context) throws IOException {
        SQLiteDatabase db = new SmartTimerSqliteOpenHelper(context).getWritableDatabase();
        return db;
    }
}
