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

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by ZD on 14/9/17.
 */

public class calendar extends AppCompatActivity{

    private static final String TAG = "calendar";
    private CalendarView calendarView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);

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
}
