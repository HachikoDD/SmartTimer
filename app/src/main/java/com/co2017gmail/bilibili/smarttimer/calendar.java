package com.co2017gmail.bilibili.smarttimer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
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

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by ZD on 14/9/17.
 */

public class calendar extends AppCompatActivity{

    private static final String TAG = "calendar";
    private CalendarView calendarView;
    SimpleDateFormat curYear = new SimpleDateFormat("dd/MM/yy");
    SimpleDateFormat curMonth = new SimpleDateFormat("MM");
    SimpleDateFormat curDay = new SimpleDateFormat("dd");
    Date currentTime = Calendar.getInstance().getTime();
    String Year = curYear.format(currentTime);
    String Month = curMonth.format(currentTime);
    String Day = curDay.format(currentTime);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);

        Log.d("calendar", Day+"/"+Month+"/"+Year);
        calendarView = (CalendarView) findViewById(R.id.calendarView);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                String date = (i1+1) + "/" + i2 + "/" + i;
//                Date strDate = curYear.parse(date);
//                if (System.currentTimeMillis() > strDate.getTime()) {
//
//                }
                Intent intent = new Intent(calendar.this,detail_summary.class);
                intent.putExtra("date", date);
                startActivity(intent);
            }
        });

//        final ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(false);
//        actionBar.setTitle(null);
//
//        compactCalendar = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
//        compactCalendar.setUseThreeLetterAbbreviation(true);
//
//        Event ev1 = new Event(Color.RED, 1504321597, "test");
//        compactCalendar.addEvent(ev1);
//
//        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
//            @Override
//            public void onDayClick(Date dateClicked) {
//                Context context = getApplicationContext();
//
//                if(dateClicked.toString().compareTo("")!=0){
//                    Toast.makeText(context, "test", Toast.LENGTH_SHORT).show();
//                }else{
//                    Toast.makeText(context, "test2", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onMonthScroll(Date firstDayOfNewMonth) {
//                actionBar.setTitle(dateFormatMonth.format(firstDayOfNewMonth));
//            }
//        });

        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(calendar.this, home_screen.class));
                        break;
                    case R.id.schedule:

                        break;
                    case R.id.me:

                        break;
                }
                return true;
            }

        };
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.BottomNavigation);
        navigation.setSelectedItemId(R.id.summary);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
}
