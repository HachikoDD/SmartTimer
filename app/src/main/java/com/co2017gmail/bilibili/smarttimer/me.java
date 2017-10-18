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
import android.widget.ImageView;
import android.provider.MediaStore;
import android.net.Uri;
import android.database.sqlite.SQLiteDatabase;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Irene on 6/10/17.
 */

public class me extends AppCompatActivity implements View.OnClickListener {

    private static final int RESULT_LOAD_IMAGE = 1;
    private static SeekBar seekBar;
    private static TextView textView;
    UserDB userDB;
    ImageView profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_me);

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
        //upload = (Button) findViewById(R.id.upload);

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
        seekBar.setMax(180);
        textView = (TextView) findViewById(R.id.time_to_show);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress_valule;

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                progress_valule = i;

                textView.setText(progress_valule+"/"+seekBar.getMax());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                textView.setText(progress_valule+"/"+seekBar.getMax());
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                textView.setText(progress_valule+"/"+seekBar.getMax());
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

}
