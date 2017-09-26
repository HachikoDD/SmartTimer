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
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.util.Date;
import java.util.Locale;

/**
 * Created by ZD on 14/9/17.
 */

public class calendar extends AppCompatActivity{

    private TextView mTextMessage;
    CompactCalendarView compactCalendar;
    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat ("MMMM-yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);

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
                        mTextMessage.setText(R.string.title_home);
                        startActivity(new Intent(calendar.this, home_screen.class));
                        break;
                    case R.id.schedule:
                        mTextMessage.setText(R.string.title_schedule);
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
