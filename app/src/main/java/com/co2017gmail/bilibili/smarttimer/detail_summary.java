package com.co2017gmail.bilibili.smarttimer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.co2017gmail.bilibili.smarttimer.R.id.container;

/**
 * Created by ZD on 27/9/17.
 */

public class detail_summary  extends AppCompatActivity {

    private static final String TAG = "detail_summary";
    private Button btnGoCalendar;
    private TextView theDate;
    UsageDB usageDB;
    SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat df3 = new SimpleDateFormat("dd/MM/yy");
    TextView detail_score;
    TextView waste_time;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_summary);
        theDate = (TextView) findViewById(R.id.tvDate);
        btnGoCalendar = (Button) findViewById(R.id.btnGoCalendar);
        detail_score = (TextView) findViewById(R.id.score);
        waste_time = (TextView) findViewById(R.id.wasted_time);

        Intent incoming = getIntent();
        String date = incoming.getStringExtra("date");
        try {
            Date incoming_date = df2.parse(date);
            String date_DB = df3.format(incoming_date);
            Usage usages = usageDB.find(getApplicationContext(),date_DB);
            if(usages!=null){
                Integer score = usages.score;
                detail_score.setText(score.toString());
                detail_score.setTextColor(getTextColor(score));
                Long waste = usages.totalUsage;
                waste_time.setText(toUsageTime_Min(waste)+" "+" "+toUsageTime_Sceond(waste));

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        theDate.setText(date);

        btnGoCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(detail_summary.this, calendar.class));
            }
        });

    }

    private  int getTextColor(int progress){
        int result;
        if(0<= progress && progress <20){
            result = getResources().getColor(R.color.red);
        }
        else if(20<= progress && progress <40){
            result = getResources().getColor(R.color.yellow);
        }
        else
            result = getResources().getColor(R.color.green);
        return  result;
    }

    protected static String toUsageTime_Min(Long time) {
        long TimeInforground = time;
        int minutes = 500, hours = 500, seconds = 500;
        seconds = (int) (TimeInforground / 1000) % 60;
        minutes = (int) ((TimeInforground / (1000 * 60)) % 60);
        hours = (int) ((TimeInforground / (1000 * 60 * 60)) % 24);
        Integer result = hours*60 + minutes;
        return result.toString()+"m";
    }

    protected static String toUsageTime_Sceond(Long time) {
        long TimeInforground = time;
        int minutes = 500, hours = 500, seconds = 500;
        seconds = (int) (TimeInforground / 1000) % 60;
        Integer result = seconds;
        return  result.toString()+"s";
    }

}
