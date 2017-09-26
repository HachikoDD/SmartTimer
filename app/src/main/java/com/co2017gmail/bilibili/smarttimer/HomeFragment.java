package com.co2017gmail.bilibili.smarttimer;


import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.app.usage.UsageStatsManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;



/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    

    //NOT NEW
    public HomeFragment() {
        // Required empty public constructor
    }

    private TextView mTextMessage;
    private TextView txtTimerHour, txtTimerMinute, txtTimerSecond;
    private TextView tvEvent, tvDate;
    private Handler handler;
    private Runnable runnable;


//    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
//            = new BottomNavigationView.OnNavigationItemSelectedListener() {
//
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            switch (item.getItemId()) {
//                case R.id.home:
//                    mTextMessage.setText(R.string.title_home);
//                    HomeFragment homeFragment = new HomeFragment();
//                    android.app.FragmentManager manager1 = getFragmentManager();
//                    manager1.beginTransaction().replace(R.id.contentLayoutHome, homeFragment, homeFragment.getTag()).commit();
//                    return true;
//                case R.id.schedule:
//                    mTextMessage.setText(R.string.title_schedule);
//                    return true;
//                case R.id.summary:
//                    mTextMessage.setText(R.string.title_summary);
//                    CalendarFragment calendarFragment = new CalendarFragment();
//                    android.app.FragmentManager manager3 = getFragmentManager();
//                    manager3.beginTransaction().replace(R.id.contentLayoutHome, calendarFragment, calendarFragment.getTag()).commit();
//                    return true;
//                case R.id.me:
//                    mTextMessage.setText(R.string.title_me);
//                    return true;
//            }
//            return false;
//        }
//
//    };

    public void currentDate(){
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());


        tvDate.setText(currentDateTimeString);
    }

    public void countDownStart() {
        handler = new Handler();

        runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this,1000);
                try{
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                    // Only in this format //YYYY-MM-DD
                    Date futureDate = dateFormat.parse("2017-09-16");  //Change to hour format
                    Date currentDate = new Date();

                    if (!currentDate.after(futureDate)) {
                        long diff = futureDate.getTime()
                                - currentDate.getTime();
                        long hours = diff / (60 * 60 * 1000);
                        diff -= hours * (60 * 60 * 1000);
                        long minutes = diff / (60 * 1000);
                        diff -= minutes * (60 * 1000);
                        long seconds = diff / 1000;

                        txtTimerHour.setText("" + String.format("%02d", hours));
                        txtTimerMinute.setText(""
                                + String.format("%02d", minutes));
                        txtTimerSecond.setText(""
                                + String.format("%02d", seconds));
                    } else {
                        tvEvent.setVisibility(View.VISIBLE);
                        tvEvent.setText("Time OUT!");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 1 * 1000);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


//        if(savedInstanceState == null){
//            getFragmentManager().beginTransaction()
//                    .add(R.id.container, HomeFragment.newInstance())
//                    .commit();
//        }
    }


    //NOT NEW
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        txtTimerHour = (TextView) view.findViewById(R.id.tv_timer_hour);
        txtTimerMinute = (TextView) view.findViewById(R.id.tv_timer_minute);
        txtTimerSecond = (TextView) view.findViewById(R.id.tv_timer_second);
        tvEvent = (TextView) view.findViewById(R.id.tvEvent);
        tvDate = (TextView) view.findViewById(R.id.tvDate);
        currentDate();
        countDownStart();

//        mTextMessage = (TextView) view.findViewById(R.id.message);
//        BottomNavigationView navigation = (BottomNavigationView) view.findViewById(R.id.BottomNavigation);
//        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        return view;
    }

}
