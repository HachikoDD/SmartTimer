package com.co2017gmail.bilibili.smarttimer;

import android.content.Intent;
import android.support.design.widget.BottomNavigationView;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.support.v7.app.AppCompatActivity;
import android.os.Handler;
import android.view.View;
import java.text.DateFormat;
import java.util.Date;
import java.text.SimpleDateFormat;

public class home_screen extends AppCompatActivity {

    private TextView mTextMessage;
    private TextView txtTimerHour, txtTimerMinute, txtTimerSecond;
    private TextView tvEvent, tvDate;
    private Handler handler;
    private Runnable runnable;



//    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
//            = new BottomNavigationView.OnNavigationItemSelectedListener() {
//
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            switch (item.getItemId()) {
//                case R.id.home:
//                    mTextMessage.setText(R.string.title_home);
//                    HomeFragment homeFragment = new HomeFragment();
//                    android.app.FragmentManager manager1 = getFragmentManager();
//                    manager1.beginTransaction().replace(R.id.contentLayoutHome, homeFragment, homeFragment.getTag()).commit();
//                    break;
//                case R.id.schedule:
//                    mTextMessage.setText(R.string.title_schedule);
//                    break;
//                case R.id.summary:
//                    mTextMessage.setText(R.string.title_summary);
//                    startActivity(new Intent(home_screen.this, calendar.class));
//                    CalendarFragment calendarFragment = new CalendarFragment();
//                    android.app.FragmentManager manager3 = getFragmentManager();
//                    manager3.beginTransaction().replace(R.id.contentLayoutHome, calendarFragment, calendarFragment.getTag()).commit();
//                    break;
//                case R.id.me:
//                    mTextMessage.setText(R.string.title_me);
//                    break;
//            }
//            return true;
//        }
//
//    };


//    public void currentDate(){
//        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
//
//
//        tvDate.setText(currentDateTimeString);
//    }
//
//    public void countDownStart() {
//        handler = new Handler();
//
//        runnable = new Runnable() {
//            @Override
//            public void run() {
//                handler.postDelayed(this,1000);
//                try{
//                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//
//                    // Only in this format //YYYY-MM-DD
//                    Date futureDate = dateFormat.parse("2017-09-16");  //Change to hour format
//                    Date currentDate = new Date();
//
//                    if (!currentDate.after(futureDate)) {
//                        long diff = futureDate.getTime()
//                                - currentDate.getTime();
//                        long hours = diff / (60 * 60 * 1000);
//                        diff -= hours * (60 * 60 * 1000);
//                        long minutes = diff / (60 * 1000);
//                        diff -= minutes * (60 * 1000);
//                        long seconds = diff / 1000;
//
//                        txtTimerHour.setText("" + String.format("%02d", hours));
//                        txtTimerMinute.setText(""
//                                + String.format("%02d", minutes));
//                        txtTimerSecond.setText(""
//                                + String.format("%02d", seconds));
//                    } else {
//                        tvEvent.setVisibility(View.VISIBLE);
//                        tvEvent.setText("Time OUT!");
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//        handler.postDelayed(runnable, 1 * 1000);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        txtTimerHour = (TextView) findViewById(R.id.tv_timer_hour);
        txtTimerMinute = (TextView) findViewById(R.id.tv_timer_minute);
        txtTimerSecond = (TextView) findViewById(R.id.tv_timer_second);
        tvEvent = (TextView) findViewById(R.id.tvEvent);
        tvDate = (TextView) findViewById(R.id.tvDate);
        //currentDate();
        //countDownStart();

        if(savedInstanceState == null){
            getFragmentManager().beginTransaction()
                    .add(R.id.container, AppUsageStatisticsFragment.newInstance())
                    .commit();
        }

//        BottomNavigationView bottomNavigationView =(BottomNavigationView) findViewById(R.id.BottomNavigation);
//
//        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.schedule:
//                        break;
//                    case R.id.summary:
//                        mTextMessage.setText(R.string.title_summary);
//                        startActivity(new Intent(home_screen.this, calendar.class));
//                        break;
//                    case R.id.me:
//                        mTextMessage.setText(R.string.title_me);
//                        break;
//                }
//                return true;
//            }
//        });

        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.schedule:
                        break;
                    case R.id.summary:
                        mTextMessage.setText(R.string.title_summary);
                        startActivity(new Intent(home_screen.this, calendar.class));
                        break;
                    case R.id.me:
                        mTextMessage.setText(R.string.title_me);
                        break;
                }
                return true;
            }

        };
        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.BottomNavigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
