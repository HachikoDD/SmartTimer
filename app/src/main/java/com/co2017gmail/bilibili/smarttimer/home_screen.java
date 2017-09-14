package com.co2017gmail.bilibili.smarttimer;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.BottomNavigationView;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

public class home_screen extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.schedule:
                    mTextMessage.setText(R.string.title_schedule);
                    return true;
                case R.id.summary:
                    mTextMessage.setText(R.string.title_summary);
                    CalendarFragment calendarFragment = new CalendarFragment();
                    android.app.FragmentManager manager3 = getFragmentManager();
                    manager3.beginTransaction().replace(R.id.contentLayoutHome, calendarFragment, calendarFragment.getTag()).commit();
                    return true;
                case R.id.me:
                    mTextMessage.setText(R.string.title_me);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.BottomNavigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
