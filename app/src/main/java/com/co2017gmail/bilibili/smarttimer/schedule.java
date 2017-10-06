package com.co2017gmail.bilibili.smarttimer;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ContentFrameLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

public class schedule extends AppCompatActivity {

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);


        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(schedule.this, home_screen.class));
                        break;
                    case R.id.summary:
                        startActivity(new Intent(schedule.this, calendar.class));
                        break;
                    case R.id.me:
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
                Intent intent = new Intent(context, createNew.class);
                startActivity(intent);
            }
        });
    }

    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        switch (view.getId()) {
            case R.id.checkbox_task1:
                if (checked) {

                }
        }
    }
}
