package com.co2017gmail.bilibili.smarttimer;

import android.app.Fragment;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by ZD on 21/10/17.
 */

public class SummaryFragment extends Fragment {

    SummaryListAdapter summaryListAdapter;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;

    public static SummaryFragment newInstance() {
        SummaryFragment fragment = new SummaryFragment();
        return fragment;
    }

    public SummaryFragment() {
        //Required an empty constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_summary_list, container, false);
    }


    @Override
    public void onViewCreated(View rootView, Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);
        summaryListAdapter = new SummaryListAdapter();
        mRecyclerView = rootView.findViewById(R.id.recyclerview_summary_list);
        mLayoutManager = mRecyclerView.getLayoutManager();
        mRecyclerView.scrollToPosition(0);
        mRecyclerView.setAdapter(summaryListAdapter);
        List<Usage> SummaryList = UsageDB.findAll(getContext());
        Collections.sort(SummaryList, new EvaluationComparator());
        summaryListAdapter.setCustomUsageStatsList(SummaryList);
        summaryListAdapter.notifyDataSetChanged();
        mRecyclerView.scrollToPosition(0);
    }

    private static class EvaluationComparator implements Comparator<Usage> {
        @Override
        public int compare(Usage left, Usage right) {
            return Long.compare(right.score, left.score);
        }
    }


}
