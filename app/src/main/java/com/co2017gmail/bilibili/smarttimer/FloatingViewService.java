package com.co2017gmail.bilibili.smarttimer;

import android.app.ActivityManager;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;


public class FloatingViewService extends Service implements View.OnClickListener {

    private WindowManager mWindowManager;
    private View mFloatingView;
    private View collapsedView;
    private View expandedView;
    private Handler handler;
    private Runnable runnable;
    TextView min_left;
    TextView second_left;
    TextView h_left;
    TextView percentage;
    UsageDB usageDB;
    Integer user_set_limiation;
    SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yy");
    SimpleDateFormat df3 = new SimpleDateFormat("dd/MM/yy/HH/mm", Locale.ENGLISH);
    String dateTime = df2.format(new Date());
    ArrayList<working_time> working_time_list = new ArrayList<>();
    ArrayList<String> disturbing_app_list = new ArrayList<>();

    public FloatingViewService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();


        //getting the widget layout from xml using layout inflater
        mFloatingView = LayoutInflater.from(this).inflate(R.layout.layout_floating_widget, null);

        //setting the layout parameters
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);


        //getting windows services and adding the floating view to it
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mFloatingView, params);


        //getting the collapsed and expanded view from the floating view
        collapsedView = mFloatingView.findViewById(R.id.layoutCollapsed);
        expandedView = mFloatingView.findViewById(R.id.layoutExpanded);


        //adding click listener to close button and expanded view
        mFloatingView.findViewById(R.id.buttonClose).setOnClickListener(this);
        expandedView.setOnClickListener(this);

        //adding an touchlistener to make drag movement of the floating widget
        mFloatingView.findViewById(R.id.relativeLayoutParent).setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;

                    case MotionEvent.ACTION_UP:
                        //when the drag is ended switching the state of the widget
                        collapsedView.setVisibility(View.GONE);
                        expandedView.setVisibility(View.VISIBLE);
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        //this code is helping the widget to move around the screen with fingers
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);
                        mWindowManager.updateViewLayout(mFloatingView, params);
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatingView != null) mWindowManager.removeView(mFloatingView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layoutExpanded:
                //switching views
                collapsedView.setVisibility(View.VISIBLE);
//                countDownStart();
                expandedView.setVisibility(View.GONE);
                break;

            case R.id.buttonClose:
                //closing the widget
                stopSelf();
                break;
        }
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
    protected static Integer toUsageTime_Hour(Long time, Integer user_set_limiation) {
        long TimeInforground = time;
        int minutes = 500, hours = 500, seconds = 500;
        hours = (int) ((TimeInforground / (1000 * 60 * 60)) % 24);
        Integer result = hours;
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
                        min_left = (TextView)  mFloatingView.findViewById(R.id.tvminute);
                        second_left = (TextView) mFloatingView.findViewById(R.id.tvsecond);
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
                            wait(500);
                            handler.removeCallbacks(runnable);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
        handler.postDelayed(runnable, 1 * 300);
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

}