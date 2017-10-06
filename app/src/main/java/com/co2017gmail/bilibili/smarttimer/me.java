package com.co2017gmail.bilibili.smarttimer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;


/**
 * Created by Irene on 6/10/17.
 */

public class me extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_me);

        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(me.this, home_screen.class));
                        break;
                    case R.id.schedule:
                        startActivity(new Intent(me.this, schedule.class));
                        break;
                    case R.id.summary:
                        startActivity(new Intent(me.this, calendar.class));
                        break;
                }
                return true;
            }

        };
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.BottomNavigation);
        navigation.setSelectedItemId(R.id.me);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
}
