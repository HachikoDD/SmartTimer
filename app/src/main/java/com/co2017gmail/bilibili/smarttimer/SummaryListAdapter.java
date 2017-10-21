package com.co2017gmail.bilibili.smarttimer;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * Provide views to RecyclerView with the directory entries.
 */
public class SummaryListAdapter extends RecyclerView.Adapter<SummaryListAdapter.ViewHolder> {

    private List<Usage> mSummaryList = new ArrayList<>();
    ApplicationDB applicationDB;
    static UsageDB usageDB;
    static Usage usage ;



    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView Date;
        private final TextView Score;
        private final TextView Evaluation;


        public ViewHolder(View v) {
            super(v);
            Date = (TextView) v.findViewById(R.id.summary_date);
            Score = (TextView) v.findViewById(R.id.summary_score);
            Evaluation = (TextView) v.findViewById(R.id.summary_evaluation);
        }

        public TextView getDate() {
            return Date;
        }

        public TextView getScore() {
            return Score;
        }

        public TextView getEvaluation() {
            return Evaluation;
        }
    }

    public SummaryListAdapter() {
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.summary_row, viewGroup, false);
        SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yy");
        String dateTime = df2.format(new Date());
        usage = usageDB.find(viewGroup.getContext(),dateTime);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Context context= viewHolder.getDate().getContext();
        viewHolder.getDate().setText(mSummaryList.get(position).dateTime);
        Integer score = (mSummaryList.get(position).score);
        viewHolder.getScore().setText(score.toString());
        viewHolder.getScore().setTextColor(ContextCompat.getColor(context, getTextColor(score)));
        viewHolder.getEvaluation().setText(getEvaluation(score));
        viewHolder.getEvaluation().setTextColor(ContextCompat.getColor(context, getTextColor(score)));
    }

    @Override
    public int getItemCount() {
        return mSummaryList.size();
    }

    public void setCustomUsageStatsList(List<Usage> SummaryList) {
        mSummaryList = SummaryList;
    }

    private  int getTextColor(int progress){
        int result;
        if(0<= progress && progress <65){
            result = R.color.red;
        }
        else if(65<= progress && progress <80){
            result = R.color.yellow;
        }
        else
            result = R.color.green;
        return  result;
    }

    private  String getEvaluation(int progress){
        String result;
        if(0<= progress && progress <65){
            result = "Bad!";
        }
        else if(65<= progress && progress <80){
            result = "Good!";
        }
        else
            result = "EXCELLENT!";
        return  result;
    }

}

