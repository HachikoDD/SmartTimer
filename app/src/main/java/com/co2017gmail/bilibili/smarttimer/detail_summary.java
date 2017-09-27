package com.co2017gmail.bilibili.smarttimer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by ZD on 27/9/17.
 */

public class detail_summary  extends AppCompatActivity {

    private static final String TAG = "detail_summary";
    private Button btnGoCalendar;
    private TextView theDate;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_summary);
        theDate = (TextView) findViewById(R.id.tvDate);
        btnGoCalendar = (Button) findViewById(R.id.btnGoCalendar);

        Intent incoming = getIntent();
        String date = incoming.getStringExtra("date");
        theDate.setText(date);

        btnGoCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(detail_summary.this, calendar.class));
            }
        });

    }
}
