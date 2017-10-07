package com.co2017gmail.bilibili.smarttimer;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class createNew extends AppCompatActivity {
    Button button;
    Button button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new);
        okListenerOnButton();
        cancelListenerOnButton();
        Intent intent = getIntent();
    }

//    @Override
    public void okListenerOnButton() {
        final Context context = this;
        button = (Button) findViewById(R.id.ok);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, schedule.class);
                startActivity(intent);
            }
        });
    }

//    @Override
    public void cancelListenerOnButton() {
        final Context context = this;
        button2 = (Button) findViewById(R.id.cancel);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, schedule.class);
                startActivity(intent);
            }
        });
    }

}
