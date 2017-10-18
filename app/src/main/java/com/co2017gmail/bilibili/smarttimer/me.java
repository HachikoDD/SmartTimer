package com.co2017gmail.bilibili.smarttimer;

import android.app.Application;
import android.app.usage.UsageStats;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
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
import java.util.ArrayList;
import java.util.List;


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


        if(savedInstanceState == null){
            getFragmentManager().beginTransaction()
                    .add(R.id.container, AppUsageStatisticsFragment.newInstance())
                    .commit();
        }
        seekbar();



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
}
