package com.co2017gmail.bilibili.smarttimer;


import android.annotation.SuppressLint;
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

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_calendar, container, false);

//        final ActionBar actionBar = g

        return view;
    }

}
