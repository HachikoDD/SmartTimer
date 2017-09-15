package com.co2017gmail.bilibili.smarttimer;


import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;

import java.util.Locale;


import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class CalendarFragment extends Fragment {

    CompactCalendarView compactCalendar;
    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat ("MMMM-yyyy", Locale.getDefault());

    public CalendarFragment()  {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        Event ev1 = new Event(Color.RED, 1504321597, "test");
//        compactCalendar.addEvent(ev1);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.calendar, container, false);
    }
}
