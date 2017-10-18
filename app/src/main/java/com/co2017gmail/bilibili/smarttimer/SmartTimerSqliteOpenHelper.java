package com.co2017gmail.bilibili.smarttimer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

    public class SmartTimerSqliteOpenHelper extends SQLiteOpenHelper {
        public SmartTimerSqliteOpenHelper(Context context){
            super(context,"smart_timer.db",null,3);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO Auto-generated method stub
            db.execSQL("create table "+SqliteDBField.App.TABLE+"(_id integer primary key autoincrement "
                    +  ","+SqliteDBField.App.APPNAME+" varchar(20) "
                    +  ","+SqliteDBField.App.APPUSAGE+" varchar(20) "
                    +  ","+SqliteDBField.App.APPMONITORSWITCH+" integer )");

            db.execSQL("create table "+SqliteDBField.User.TABLE+"(_id integer primary key autoincrement "
                    +  ","+SqliteDBField.User.USERNAME+" varchar(20) "
                    +  ","+SqliteDBField.User.PASSWORD+" varchar(20) "
                    +  ","+SqliteDBField.User.EMAIL+" varchar(20) "
                    +  ","+SqliteDBField.User.PHONENUMBER+" varchar(20) "
                    +  ","+SqliteDBField.User.AVATOR+" varchar(30) "
                    +  ","+SqliteDBField.User.DAILYUSAGELIMIT+" integer"
                    +  ","+SqliteDBField.User.NOTIFICATIONSWITCH+" integer "
                    +  ","+SqliteDBField.User.RESTRICTIONSWITCH+" integer "
                    +  ","+SqliteDBField.User.WEDGETSWITCH+" integer )");

            db.execSQL("create table "+SqliteDBField.Usage.TABLE+"(_id integer primary key autoincrement "
                    +  ","+SqliteDBField.Usage.DATETIME+" varchar(30) "
                    +  ","+SqliteDBField.Usage.TOTALUSAGE+" varchar(20) "
                    +  ","+SqliteDBField.Usage.WASTEDTIME+" varchar(20) "
                    +  ","+SqliteDBField.Usage.SCORE+" integer )");

            db.execSQL("create table "+SqliteDBField.Events.TABLE+"(_id integer primary key autoincrement "
                    +  ","+SqliteDBField.Events.EVENTNAME+" varchar(20) "
                    +  ","+SqliteDBField.Events.EVENTSTARTTIME+" varchar(20) "
                    +  ","+SqliteDBField.Events.EVENTFINISHTIME+" varchar(20) "
                    +  ","+SqliteDBField.Events.EVENTSTATUSTIME+" integer )");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub

        }

    }

