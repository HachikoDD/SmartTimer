package com.co2017gmail.bilibili.smarttimer;

import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by ZD on 14/9/17.
 */

public class calendar extends AppCompatActivity{

    private static final String TAG = "calendar";
    private CalendarView calendarView;
    public Handler handler_back;
    public Runnable runnable_back;
    ApplicationDB applicationDB;
    EventsDB eventsDB;
    UserDB userDB;
    UsageDB usageDB;
    Integer user_set_limiation;
    java.text.SimpleDateFormat df2 = new java.text.SimpleDateFormat("dd/MM/yy");
    java.text.SimpleDateFormat df3 = new java.text.SimpleDateFormat("dd/MM/yy/HH/mm", Locale.ENGLISH);
    String dateTime = df2.format(new Date());
    String nowTime = df3.format(new Date());
    ArrayList<working_time> working_time_list = new ArrayList<>();
    ArrayList<String> disturbing_app_list = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);

        ArrayList<Events> events_today = new ArrayList<>();
        ArrayList<Events> events_today_filter = new ArrayList<>();
        working_time_list = new ArrayList<>();
        Context context = getApplicationContext();
        if(eventsDB.findAll(getApplicationContext())!=null)
            events_today = (ArrayList<Events>) eventsDB.findAll(getApplicationContext());

        for(Events events: events_today){
            working_time_list = new ArrayList<>();
            if(events.eventStatusTime.equals("ON")) {
                events_today_filter.add(events);
                Log.i("Event_NAME:", events.eventName);
                Log.i("Event_TIME:", events.eventStartTime + " TO " + events.eventFinishTime);
                try {
                    Date date_start = df3.parse(events.eventStartTime);
                    Date date_end = df3.parse(events.eventFinishTime);
                    working_time_list.add(new working_time(date_start,date_end));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            StartBackGround();
        }

        if(savedInstanceState == null){
            getFragmentManager().beginTransaction()
                    .add(R.id.container_summary, SummaryFragment.newInstance())
                    .commit();
        }

        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        handler_back.removeCallbacks(runnable_back);
                        startActivity(new Intent(calendar.this, home_screen.class));
                        break;
                    case R.id.schedule:
                        handler_back.removeCallbacks(runnable_back);
                        startActivity(new Intent(calendar.this, schedule.class));
                        break;
                    case R.id.me:
                        handler_back.removeCallbacks(runnable_back);
                        startActivity(new Intent(calendar.this, me.class));
                        break;
                }
                return true;
            }

        };
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.BottomNavigation);
        navigation.setSelectedItemId(R.id.summary);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        calendarView = (CalendarView) findViewById(R.id.calendarView);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange (@NonNull CalendarView calendarView, int i, int i1, int i2) {
                String date = i2 + "/" + (i1+1) + "/" + i;
                Context context = getApplicationContext();
                CharSequence text = "Please Select Correct Day!";
                int duration = Toast.LENGTH_SHORT;
                Log.d(TAG, "now = " + new SimpleDateFormat("dd/MM/yy").format(new Date()));
                Log.d(TAG, "selected = " + date);
                try {
                    SimpleDateFormat  format = new SimpleDateFormat("dd/MM/yy");
                    Date strDate = format.parse(date);
                    if (new Date().after(strDate)) {
                        Intent intent = new Intent(calendar.this,detail_summary.class);
                        intent.putExtra("date", date);
                        startActivity(intent);
                    }
                    else {
                        Toast toast = Toast.makeText(context, text ,duration);
                        toast.show();
                    }
                } catch (ParseException e) {
                    //handle exception
                }
            }
        });
    }

    public class working_time{
        private Date start;
        private Date end;

        private working_time(Date start, Date end){
            this.start = start;
            this.end = end;
        }

        private Date get_from(){
            return start;
        }

        private Date get_end(){
            return end;
        }
    }

    private boolean CheckisPeriod(ArrayList<working_time> working_time_list){
        Date now = new Date();
        for(working_time time_range: working_time_list){
            Date start = time_range.get_from();
            Date end = time_range.get_end();
//            Log.i("Time_now", df3.format(now));
//            Log.i("Time_start", df3.format(start));
//            Log.i("Time_end", df3.format(end));
            if(now.before(end)&&now.after(start))
                Log.i("Time_end", "True!");
            return true;
        }
        return false;
    }

    public void StartBackGround(){
        if(handler_back==null&&handler_back==null) {
            handler_back = new Handler();
            final int delay = 1000; //milliseconds
            handler_back.postDelayed(runnable_back =new Runnable() {
                public void run() {
                    for (App app : applicationDB.findAll(getApplicationContext())) {
                        if (app.monitorSwitch) {
                            disturbing_app_list.add(app.name);
                        }
                    }
                    String app_name_top = getAppNameFromPackage(getTopAppName(getApplicationContext()), getApplicationContext());
                    user_set_limiation = userDB.find(getApplicationContext(), "BiliBili").dailyUsageLimit * 60;
                    if(CheckisPeriod(working_time_list)) {
                        Usage today_uasge = usageDB.find(getApplicationContext(), dateTime);
                        if (disturbing_app_list.contains(app_name_top)) {
                            String min = toUsageTime_Min(today_uasge.totalUsage, user_set_limiation).toString() + "m";
                            String sceond = toUsageTime_Sceond(today_uasge.totalUsage, user_set_limiation).toString() + "s";
                            today_uasge.totalUsage = today_uasge.totalUsage + 1000;
                            usageDB.update(getApplicationContext(), today_uasge);
                            Log.i("Back_ground_RUNING", app_name_top);
                            Log.i("Back_ground_Lefttime", min + sceond);
                        }
                    }
                    else{
                        Usage today_uasge = usageDB.find(getApplicationContext(), dateTime);
                        if (disturbing_app_list.contains(app_name_top)&&today_uasge!=null) {
                            String min = toUsageTime_Min(today_uasge.totalUsage, user_set_limiation).toString() + "m";
                            String sceond = toUsageTime_Sceond(today_uasge.totalUsage, user_set_limiation).toString() + "s";
                            today_uasge.totalUsage = today_uasge.totalUsage - 1000;
                            usageDB.update(getApplicationContext(), today_uasge);
                            Log.i("Back_ground_RUNING_NO", app_name_top);
                            Log.i("Back_ground_Lefttime_NO", min + sceond);
                        }
                    }
                    handler_back.postDelayed(this, delay);
                }
            }, delay);

        }
    }

    public static String getTopAppName(Context context) {
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String strName = "";
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                strName = getLollipopFGAppPackageName(context);
            } else {
                strName = mActivityManager.getRunningTasks(1).get(0).topActivity.getClassName();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strName;
    }

    private static String getLollipopFGAppPackageName(Context ctx) {

        try {
            UsageStatsManager usageStatsManager = (UsageStatsManager) ctx.getSystemService(Context.USAGE_STATS_SERVICE);
            long milliSecs = 24 * 60 * 60 * 1000;
            Date date = new Date();
            List<UsageStats> queryUsageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, date.getTime() - milliSecs, date.getTime());
            if (queryUsageStats.size() > 0) {
                Log.i("LPU", "queryUsageStats size: " + queryUsageStats.size());
            }
            long recentTime = 0;
            String recentPkg = "";
            for (int i = 0; i < queryUsageStats.size(); i++) {
                UsageStats stats = queryUsageStats.get(i);
                if (i == 0 && !"org.pervacio.pvadiag".equals(stats.getPackageName())) {
                    Log.i("LPU", "PackageName: " + stats.getPackageName() + " " + stats.getLastTimeStamp());
                }
                if (stats.getLastTimeStamp() > recentTime) {
                    recentTime = stats.getLastTimeStamp();
                    recentPkg = stats.getPackageName();
                }
            }
            return recentPkg;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private String getAppNameFromPackage(String packageName, Context context) {
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> pkgAppsList = context.getPackageManager()
                .queryIntentActivities(mainIntent, 0);
        for (ResolveInfo app : pkgAppsList) {
            if (app.activityInfo.packageName.equals(packageName)) {
                return app.activityInfo.loadLabel(context.getPackageManager()).toString();
            }
        }
        return null;
    }

    protected static Integer toUsageTime_Min(Long time, Integer user_set_limiation) {
        long TimeInforground = time;
        int minutes = 500, hours = 500, seconds = 500;
        seconds = (int) (TimeInforground / 1000) % 60;
        minutes = (int) ((TimeInforground / (1000 * 60)) % 60);
        hours = (int) ((TimeInforground / (1000 * 60 * 60)) % 24);
        Integer result = ((user_set_limiation - minutes*60 - hours*3600 - seconds)-(user_set_limiation - minutes*60 - hours*3600 - seconds)%60)/60;
        return result;
    }

    protected static Integer toUsageTime_Sceond(Long time, Integer user_set_limiation) {
        long TimeInforground = time;
        int minutes = 500, hours = 500, seconds = 500;
        seconds = (int) (TimeInforground / 1000) % 60;
        minutes = (int) ((TimeInforground / (1000 * 60)) % 60);
        hours = (int) ((TimeInforground / (1000 * 60 * 60)) % 24);
        Integer result = (user_set_limiation - minutes*60 - hours*3600 - seconds)%60;
        return  result;
    }
    protected static Integer toUsageTime(Long time) {
        long TimeInforground = time;
        int minutes = 500, hours = 500, seconds = 500;
        seconds = (int) (TimeInforground / 1000) % 60;
        minutes = (int) ((TimeInforground / (1000 * 60)) % 60);
        hours = (int) ((TimeInforground / (1000 * 60 * 60)) % 24);
        return  minutes*60+hours*3600+seconds;
    }

}
