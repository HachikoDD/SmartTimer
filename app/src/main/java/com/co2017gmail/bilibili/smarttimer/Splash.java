package com.co2017gmail.bilibili.smarttimer;

import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.ImageView;
import android.view.Menu;
import android.view.MenuItem;

public class Splash extends AppCompatActivity{

    private TextView tvsplash;
    private ImageView ivsplash;

    @Override
    protected void onCreate (Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_splash);
        tvsplash = (TextView) findViewById(R.id.tvsplash);
        ivsplash = (ImageView) findViewById(R.id.ivsplash);
        Animation myanim = AnimationUtils.loadAnimation(this,R.anim.mytransition);
        tvsplash.startAnimation(myanim);
        ivsplash.startAnimation(myanim);


    }
}