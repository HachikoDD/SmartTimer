package com.co2017gmail.bilibili.smarttimer;

import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ContentFrameLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.widget.CompoundButton;

public class schedule extends AppCompatActivity {

    Button button;
    CheckBox checkBox1;
    CheckBox checkBox2;
    CheckBox checkBox3;
    CheckBox checkBox4;
    public Handler handler_back;
    public Runnable runnable_back;
    ApplicationDB applicationDB;
    EventsDB eventsDB;
    UserDB userDB;
    UsageDB usageDB;
    Integer user_set_limiation;
    SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yy");
    SimpleDateFormat df3 = new SimpleDateFormat("dd/MM/yy/HH/mm", Locale.ENGLISH);
    String dateTime = df2.format(new Date());
    String nowTime = df3.format(new Date());
    ArrayList<working_time> working_time_list = new ArrayList<>();
    ArrayList<String> disturbing_app_list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        checkBox1 =(CheckBox) findViewById(R.id.checkbox_task1);
        checkBox2 =(CheckBox) findViewById(R.id.checkbox_task2);
        checkBox3 =(CheckBox) findViewById(R.id.checkbox_task3);
        checkBox4 =(CheckBox) findViewById(R.id.checkbox_task4);

        lol("a");lolb("b");lolc("c");lold("d");

        if((eventsDB.find(getApplicationContext(),"a").eventStatusTime).equals("ON")){
            checkBox1.setChecked(true);
        }else{
            checkBox1.setChecked(false);
        }
        if((eventsDB.find(getApplicationContext(),"b").eventStatusTime).equals("ON")){
            checkBox2.setChecked(true);
        }else{
            checkBox2.setChecked(false);
        }
        if((eventsDB.find(getApplicationContext(),"c").eventStatusTime).equals("ON")){
            checkBox3.setChecked(true);
        }else{
            checkBox3.setChecked(false);
        }
        if((eventsDB.find(getApplicationContext(),"d").eventStatusTime).equals("ON")){
            checkBox4.setChecked(true);
        }else{
            checkBox4.setChecked(false);
        }

        ArrayList<Events> events_today = new ArrayList<>();
        ArrayList<Events> events_today_filter = new ArrayList<>();
        working_time_list = new ArrayList<>();
        Context context = getApplicationContext();
        if(eventsDB.findAll(getApplicationContext())!=null)
            events_today = (ArrayList<Events>) eventsDB.findAll(getApplicationContext());

        for(Events events: events_today){

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


        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        handler_back.removeCallbacks(runnable_back);
                        startActivity(new Intent(schedule.this, home_screen.class));
                        break;
                    case R.id.summary:
                        handler_back.removeCallbacks(runnable_back);
                        startActivity(new Intent(schedule.this, calendar.class));
                        break;
                    case R.id.me:
                        handler_back.removeCallbacks(runnable_back);
                        startActivity(new Intent(schedule.this, me.class));
                        break;
                }
                return true;
            }

        };
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.BottomNavigation);
        navigation.setSelectedItemId(R.id.schedule);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        create_new();

    }

    public void create_new() {
        final Context context = this;
        button = (Button) findViewById(R.id.add_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Calendar cal = Calendar.getInstance();
                Intent intent = new Intent(Intent.ACTION_EDIT);
                intent.setType("vnd.android.cursor.item/event");
                intent.putExtra("beginTime", cal.getTimeInMillis());
                intent.putExtra("allDay", true);
                intent.putExtra("rrule", "FREQ=YEARLY");
                intent.putExtra("endTime", cal.getTimeInMillis() + 60 * 60 * 1000);
                intent.putExtra("title", "A Test Event from android app");
                startActivity(intent);

            }
        });

    }


    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        switch (view.getId()) {
            case R.id.checkbox_task1:
                if (checked) {
                    Events events = eventsDB.find(getApplicationContext(),"a");
                    events.eventStatusTime = "ON";
                    eventsDB.update(getApplicationContext(),events);
                    break;
                }else{
                    Events events = eventsDB.find(getApplicationContext(),"a");
                    events.eventStatusTime = "OFF";
                    eventsDB.update(getApplicationContext(),events);
                    break;
                }
            case R.id.checkbox_task2:
                if (checked) {
                    Events events = eventsDB.find(getApplicationContext(),"b");
                    events.eventStatusTime = "ON";
                    eventsDB.update(getApplicationContext(),events);
                    break;
                }else{
                    Events events = eventsDB.find(getApplicationContext(),"b");
                    events.eventStatusTime = "OFF";
                    eventsDB.update(getApplicationContext(),events);
                    break;
                }
            case R.id.checkbox_task3:
                if (checked) {
                    Events events = eventsDB.find(getApplicationContext(),"c");
                    events.eventStatusTime = "ON";
                    eventsDB.update(getApplicationContext(),events);
                    break;
                }else{
                    Events events = eventsDB.find(getApplicationContext(),"c");
                    events.eventStatusTime = "OFF";
                    eventsDB.update(getApplicationContext(),events);
                    break;
                }
            case R.id.checkbox_task4:
                if (checked) {
                    Events events = eventsDB.find(getApplicationContext(),"d");
                    events.eventStatusTime = "ON";
                    eventsDB.update(getApplicationContext(),events);
                    break;
                }else{
                    Events events = eventsDB.find(getApplicationContext(),"d");
                    events.eventStatusTime = "OFF";
                    eventsDB.update(getApplicationContext(),events);
                    break;
                }
        }
    }

    protected void lol(String name){
        if(eventsDB.find(getApplicationContext(),name)==null){
            Events events = new Events();
            events.eventStatusTime="OFF";
            events.eventName=name;
            events.eventStartTime = "19/10/17/08/47";
            events.eventFinishTime = "19/10/17/09/47";
            eventsDB.insert(getApplicationContext(),events);
        }
        else{
            Events events = eventsDB.find(getApplicationContext(),name);
            events.eventStartTime = "21/10/17/08/00";
            events.eventFinishTime = "21/10/17/10/00";
            eventsDB.update(getApplicationContext(),events);
        }
    }

    protected void lolb(String name){
        if(eventsDB.find(getApplicationContext(),name)==null){
            Events events = new Events();
            events.eventStatusTime="OFF";
            events.eventName=name;
            events.eventStartTime = "19/10/17/09/47";
            events.eventFinishTime = "19/10/17/12/47";
            eventsDB.insert(getApplicationContext(),events);
        }
        else{
            Events events = eventsDB.find(getApplicationContext(),name);
            events.eventStartTime = "21/10/17/13/01";
            events.eventFinishTime = "21/10/17/15/00";
            eventsDB.update(getApplicationContext(),events);
        }
    }

    protected void lolc(String name){
        if(eventsDB.find(getApplicationContext(),name)==null){
            Events events = new Events();
            events.eventStatusTime="OFF";
            events.eventName=name;
            events.eventStartTime = "19/10/17/09/47";
            events.eventFinishTime = "19/10/17/12/47";
            eventsDB.insert(getApplicationContext(),events);
        }
        else{
            Events events = eventsDB.find(getApplicationContext(),name);
            events.eventStartTime = "21/10/17/15/00";
            events.eventFinishTime = "21/10/17/17/00";
            eventsDB.update(getApplicationContext(),events);
        }
    }

    protected void lold(String name){
        if(eventsDB.find(getApplicationContext(),name)==null){
            Events events = new Events();
            events.eventStatusTime="OFF";
            events.eventName=name;
            events.eventStartTime = "21/10/17/09/47";
            events.eventFinishTime = "21/10/17/12/47";
            eventsDB.insert(getApplicationContext(),events);
        }
        else{
            Events events = eventsDB.find(getApplicationContext(),name);
            events.eventStartTime = "21/10/17/20/00";
            events.eventFinishTime = "21/10/17/23/00";
            eventsDB.update(getApplicationContext(),events);
        }
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
                    Usage today_uasge = usageDB.find(getApplicationContext(), dateTime);
                    if(CheckisPeriod(working_time_list)) {
                        user_set_limiation = userDB.find(getApplicationContext(), "BiliBili").dailyUsageLimit * 60;
                        if (disturbing_app_list.contains(app_name_top)) {
                            String min = toUsageTime_Min(today_uasge.totalUsage, user_set_limiation).toString() + "m";
                            String sceond = toUsageTime_Sceond(today_uasge.totalUsage, user_set_limiation).toString() + "s";
                            today_uasge.totalUsage = today_uasge.totalUsage + 1000;
                            usageDB.update(getApplicationContext(), today_uasge);
                            Log.i("Back_ground_RUNING", app_name_top);
                            Log.i("Back_ground_Lefttime", min + sceond);
                        }
                        handler_back.postDelayed(this, delay);
                    }
                    else{
                        if (disturbing_app_list.contains(app_name_top)) {
                            today_uasge.totalUsage = today_uasge.totalUsage - 1000;
                            usageDB.update(getApplicationContext(), today_uasge);
                        }
                    }
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
