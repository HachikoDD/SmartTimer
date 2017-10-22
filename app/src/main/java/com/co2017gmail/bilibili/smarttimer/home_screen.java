package com.co2017gmail.bilibili.smarttimer;

import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.support.v7.app.AppCompatActivity;
import android.os.Handler;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;


public class home_screen extends AppCompatActivity{

    private TextView txtTimerHour, txtTimerMinute, txtTimerSecond;
    private TextView tvEvent, tvDate;
    private Handler handler;
    private Runnable runnable;
    public Handler handler_back;
    public Runnable runnable_back;
    ApplicationDB applicationDB;
    EventsDB eventsDB;
    UserDB userDB;
    TextView min_left;
    TextView second_left;
    TextView percentage;
    UsageDB usageDB;
    Integer user_set_limiation;
    SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yy");
    SimpleDateFormat df3 = new SimpleDateFormat("dd/MM/yy/HH/mm", Locale.ENGLISH);
    String dateTime = df2.format(new Date());
    ArrayList<working_time> working_time_list = new ArrayList<>();
    ArrayList<String> disturbing_app_list = new ArrayList<>();


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        ArrayList<Events> events_today = new ArrayList<>();
        ArrayList<Events> events_today_filter = new ArrayList<>();
        working_time_list = new ArrayList<>();
        Context context = getApplicationContext();
        if(eventsDB.findAll(getApplicationContext())!=null)
            events_today = (ArrayList<Events>) eventsDB.findAll(getApplicationContext());
        user_set_limiation = userDB.find(getApplicationContext(),"BiliBili").dailyUsageLimit * 60;

//        min_left.setText(toUsageTime_Min(usageDB.find(getApplicationContext(), dateTime).totalUsage, user_set_limiation).toString() + "m");
//        second_left.setText(toUsageTime_Sceond(usageDB.find(getApplicationContext(), dateTime).totalUsage, user_set_limiation).toString() + "s");


//        Usage usage = new Usage();
//        usage.score = 60;
//        usage.dateTime = "19/10/17";
//        usage.wastedTime = 90061L;
//        UsageDB.insert(context,usage);


//        UsageDB.delete(context, "19/10/17");
//        UsageDB.delete(context, "18/10/17");
//        UsageDB.delete(context, "01/01/70");
//        UsageDB.delete(context, "10/18/17");
//
//        List<Usage> usages = UsageDB.findAll(context);
//        if(usages!=null)
//            for(Usage usage: usages){
//                Log.i("USAGE",usage.dateTime);
//            }



        for(Events events: events_today){
            working_time_list = new ArrayList<>();
            if(events.eventStatusTime.equals("ON")) {
                events_today_filter.add(events);
                Log.i("Event_NAME:", events.eventName);
                Log.i("Event_Now:", df3.format(new Date()));
                Log.i("Event_TIME:", events.eventStartTime + " TO " + events.eventFinishTime);
                try {
                    Date date_start = df3.parse(events.eventStartTime);
                    Date date_end = df3.parse(events.eventFinishTime);
                    working_time_list.add(new working_time(date_start,date_end));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

//        for(working_time working_time: working_time_list){
//            Log.i("Event_TIME:", working_time.get_from() + " TO " + working_time.get_end());
//        }


        if(savedInstanceState == null){
            getFragmentManager().beginTransaction()
                    .add(R.id.container_homescreen, homescreen_list.newInstance())
                    .commit();
        }
        if(usageDB.find(getApplicationContext(), dateTime)!=null) {
            if (toUsageTime(usageDB.find(getApplicationContext(), dateTime).totalUsage) < user_set_limiation)
                countDownStart();
        }
        else{
            Usage usage = new Usage();
            usage.dateTime=dateTime;
            usage.score = 0;
            usageDB.insert(getApplicationContext(), usage);
        }

         StartBackGround();


        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.schedule:
                        if(handler!=null)
                        handler.removeCallbacks(runnable);
                        handler_back.removeCallbacks(runnable_back);
                        startActivity(new Intent(home_screen.this, schedule.class));
                        break;
                    case R.id.summary:
                        if(handler!=null)
                        handler.removeCallbacks(runnable);
                        handler_back.removeCallbacks(runnable_back);
                        startActivity(new Intent(home_screen.this, calendar.class));
                        break;
                    case R.id.me:
                        if(handler!=null)
                        handler.removeCallbacks(runnable);
                        handler_back.removeCallbacks(runnable_back);
                        startActivity(new Intent(home_screen.this, me.class));
                        break;
                }
                return true;
            }

        };
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.BottomNavigation);
        navigation.setSelectedItemId(R.id.home);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
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

    public void countDownStart() {
        if(handler==null)
        handler = new Handler();
        if(runnable==null)
        runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this,300);
                try{
                    Usage usage = usageDB.find(getApplicationContext(), dateTime);
                    min_left = (TextView) findViewById(R.id.left_min);
                    second_left = (TextView) findViewById(R.id.left_second);
                    percentage = (TextView)findViewById(R.id.percentage_usage);
                    double per = (user_set_limiation-toUsageTime(usage.totalUsage));
                    usage.score = (int) (per/user_set_limiation*100);
                    UsageDB.update(getApplicationContext(),usage);
                    percentage.setText(per/user_set_limiation*100+"%");
                    percentage.setTextColor(getTextColor((int)(per/user_set_limiation*100)));
                    if(CheckisPeriod(working_time_list)) {
                        min_left.setText(toUsageTime_Min(usageDB.find(getApplicationContext(), dateTime).totalUsage, user_set_limiation).toString() + "m");
                        second_left.setText(toUsageTime_Sceond(usageDB.find(getApplicationContext(), dateTime).totalUsage, user_set_limiation).toString() + "s");
                    }else{
                        min_left.setText(toUsageTime_Min(usageDB.find(getApplicationContext(), dateTime).totalUsage, user_set_limiation).toString() + "m");
                        second_left.setText(toUsageTime_Sceond(usageDB.find(getApplicationContext(), dateTime).totalUsage, user_set_limiation).toString() + "s");
                        handler.removeCallbacks(runnable);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 1 * 300);
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
                    Usage today_uasge = usageDB.find(getApplicationContext(), dateTime);
                    if(CheckisPeriod(working_time_list)) {
                        if (disturbing_app_list.contains(app_name_top)) {
                            String min = toUsageTime_Min(today_uasge.totalUsage, user_set_limiation).toString() + "m";
                            String sceond = toUsageTime_Sceond(today_uasge.totalUsage, user_set_limiation).toString() + "s";
                            today_uasge.totalUsage = today_uasge.totalUsage + 1000;
                            usageDB.update(getApplicationContext(), today_uasge);
                            Log.i("Back_ground_RUNING", app_name_top);
                            Log.i("Back_ground_RUNING", min + sceond);
                        }
                    }
                    else{
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

    private  int getTextColor(int progress){
        int result;
        if(0<= progress && progress <30){
            result = getResources().getColor(R.color.red);
        }
        else if(30<= progress && progress <80){
            result = getResources().getColor(R.color.yellow);
        }
        else
            result = getResources().getColor(R.color.green);
        return  result;
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

    private boolean CheckisPeriod(ArrayList<working_time> working_time_list){
        Date now = new Date();
        for(working_time time_range: working_time_list){
            Date start = time_range.get_from();
            Date end = time_range.get_end();
            Log.i("Time_now", df3.format(now));
            Log.i("Time_start", df3.format(start));
            Log.i("Time_end", df3.format(end));
            if(now.before(end)&&now.after(start))
                Log.i("Time_end", "True!");
                return true;
        }
        return false;
    }

}
