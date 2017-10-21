package com.co2017gmail.bilibili.smarttimer;

import android.app.ActivityManager;
import android.app.Application;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.provider.MediaStore;
import android.net.Uri;
import android.database.sqlite.SQLiteDatabase;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * Created by Irene on 6/10/17.
 */

public class me extends AppCompatActivity implements View.OnClickListener {

    private static final int RESULT_LOAD_IMAGE = 1;
    private static SeekBar seekBar;
    private static TextView username ;
    private static TextView email;
    private static TextView phoneNumber;
    private static int dailyUsageLimit;
    private static Switch notificationSwitch;
    private static Switch restrictionSwitch;
    private static Switch wedgetSwitch;
    private static Context context;
    private static TextView textView;
    UserDB userDB;
    ImageView profile;
    Button edit;

    public Handler handler_back;
    public Runnable runnable_back;
    ApplicationDB applicationDB;
    EventsDB eventsDB;
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
        setContentView(R.layout.activity_home_me);
        context = getApplicationContext();
        username = (TextView) findViewById(R.id.username);
        final String user_name = username.getText().toString();
        notificationSwitch = (Switch) findViewById((R.id.switch_notif));
        restrictionSwitch = (Switch) findViewById(R.id.switch_restr);
        wedgetSwitch = (Switch) findViewById(R.id.switch_widget);
        edit = (Button) findViewById(R.id.imageView_edit);

        if(userDB.find(context,user_name)==null){
            User user = new User();
            user.userName = user_name;
            user.notificationSwitch = false;
            user.restrictionSwitch =false;
            user.wedgetSwitch = false;
            userDB.insert(context,user);
            notificationSwitch.setChecked(false);
            restrictionSwitch.setChecked(false);
            wedgetSwitch.setChecked(false);
        }else{
            notificationSwitch.setChecked(UserDB.find(context,user_name).notificationSwitch);
            restrictionSwitch.setChecked(UserDB.find(context,user_name).restrictionSwitch);
            wedgetSwitch.setChecked(UserDB.find(context,user_name).wedgetSwitch);
        }
        final User user = userDB.find(context,user_name);
        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    user.notificationSwitch = true;
//                    Toast.makeText(me.this,"Ture",Toast.LENGTH_SHORT).show();
                    UserDB.update(context,user);
                }
                else{
                    user.notificationSwitch = false;
//                    Toast.makeText(me.this,"False",Toast.LENGTH_SHORT).show();
                    UserDB.update(context,user);
                }
            }
        });

        restrictionSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    user.restrictionSwitch = true;
//                    Toast.makeText(me.this,"Ture",Toast.LENGTH_SHORT).show();
                    UserDB.update(context,user);
                }
                else{
                    user.restrictionSwitch = false;
//                    Toast.makeText(me.this,"False",Toast.LENGTH_SHORT).show();
                    UserDB.update(context,user);
                }
            }
        });

        wedgetSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    user.wedgetSwitch = true;
//                    Toast.makeText(me.this,"Ture",Toast.LENGTH_SHORT).show();
                    UserDB.update(context,user);
                }
                else{
                    user.wedgetSwitch = false;
//                    Toast.makeText(me.this,"False",Toast.LENGTH_SHORT).show();
                    UserDB.update(context,user);
                }
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler_back.removeCallbacks(runnable_back);
                startActivity(new Intent(me.this, EditActivity.class));
            }
        });


        if(savedInstanceState == null){
            getFragmentManager().beginTransaction()
                    .add(R.id.container, AppUsageStatisticsFragment.newInstance())
                    .commit();
        }
        seekbar();

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
                        startActivity(new Intent(me.this, home_screen.class));
                        break;
                    case R.id.schedule:
                        handler_back.removeCallbacks(runnable_back);
                        startActivity(new Intent(me.this, schedule.class));
                        break;
                    case R.id.summary:
                        handler_back.removeCallbacks(runnable_back);
                        startActivity(new Intent(me.this, calendar.class));
                        break;
                }
                return true;
            }

        };
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.BottomNavigation);
        navigation.setSelectedItemId(R.id.me);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        profile = (ImageView) findViewById(R.id.profile);

        profile.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.profile:
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
                break;
        }
    }

    public void seekbar(){
        seekBar = (SeekBar) findViewById(R.id.limited_time_seek_bar);
        final String user_name = username.getText().toString();
        final User user = UserDB.find(context,user_name);
        seekBar.setMax(180);
        seekBar.setProgress(user.dailyUsageLimit);
        textView = (TextView) findViewById(R.id.time_to_show);
        username = (TextView) findViewById(R.id.username);
        int progress = user.dailyUsageLimit;
        textView.setText(progress+"/"+seekBar.getMax());
        textView.setTextColor(getTextColor(progress));
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress_valule = user.dailyUsageLimit;
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                progress_valule = i;
                textView.setText(progress_valule+"/"+seekBar.getMax());
                textView.setTextColor(getTextColor(progress_valule));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                textView.setText(progress_valule+"/"+seekBar.getMax());
                textView.setTextColor(getTextColor(progress_valule));
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                textView.setText(progress_valule+"/"+seekBar.getMax());
                textView.setTextColor(getTextColor(progress_valule));
                user.dailyUsageLimit = progress_valule;
                UserDB.update(context,user);
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null){
            Uri selectedImage = data.getData();
            profile.setImageURI(selectedImage);
        }
    }

    private  int getTextColor(int progress){
        int result;
        if(0<= progress && progress <60){
            result = getResources().getColor(R.color.green);
        }
        else if(60<= progress && progress <120){
            result = getResources().getColor(R.color.yellow);
        }
        else
            result = getResources().getColor(R.color.red);
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
                    Usage today_uasge = usageDB.find(getApplicationContext(), dateTime);
                    if(CheckisPeriod(working_time_list)) {
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
                        Log.i("Back_ground_RUNING_NO", app_name_top);
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
