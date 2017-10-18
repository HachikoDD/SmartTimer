package com.co2017gmail.bilibili.smarttimer;

import android.widget.ImageView;

/**
 * Created by bilibili on 2017/10/11.
 */

public class SqliteDBField {
   public static class App{
        public static String TABLE = "app";
        public static String APPNAME = "appname";
        public static String APPUSAGE = "appUsage";
        public static String APPMONITORSWITCH = "appMonitorSwitch";
        public static ImageView APPICON;
    }
   public static class User{
        public static String TABLE = "user";
        public static String USERNAME = "userName";
        public static String PASSWORD = "password";
        public static String EMAIL = "email";
        public static String PHONENUMBER = "phoneNumber";
        public static String AVATOR = "avator";
        public static String DAILYUSAGELIMIT = "dailyUsageLimit";
        public static String NOTIFICATIONSWITCH = "notificationSwitch";
        public static String RESTRICTIONSWITCH = "restrictionSwitch";
        public static String WEDGETSWITCH = "wedgetSwitch";
    }
   public static class Usage{
        public static String TABLE = "usage";
        public static String DATETIME = "dateTime";
        public static String TOTALUSAGE = "totalUsage";
        public static String WASTEDTIME = "wastedTime";
        public static String SCORE = "score";
    }
   public static class Events{
        public static String TABLE = "events";
        public static String EVENTNAME = "eventName";
        public static String EVENTSTARTTIME = "eventStartTime";
        public static String EVENTFINISHTIME = "eventFinishTime";
        public static String EVENTSTATUSTIME = "eventStatusTime";
    }
}
