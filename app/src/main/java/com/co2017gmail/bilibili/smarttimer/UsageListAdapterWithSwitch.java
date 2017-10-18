package com.co2017gmail.bilibili.smarttimer;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.widget.Toast;

/**
 * Provide views to RecyclerView with the directory entries.
 */
public class UsageListAdapterWithSwitch extends RecyclerView.Adapter<UsageListAdapterWithSwitch.ViewHolder> {

    private List<CustomUsageStats> mCustomUsageStatsList = new ArrayList<>();
    private String Useage;
    ApplicationDB applicationDB;
    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mPackageName;
        private final TextView mLastTimeUsed;
        private final ImageView mAppIcon;
        private final Switch isDisturb;


        public ViewHolder(View v) {
            super(v);
            mPackageName = (TextView) v.findViewById(R.id.textview_package_name);
            mLastTimeUsed = (TextView) v.findViewById(R.id.textview_last_time_used);
            mAppIcon = (ImageView) v.findViewById(R.id.app_icon);
            isDisturb = (Switch) v.findViewById(R.id.isDisturb);

        }

        public TextView getLastTimeUsed() {
            return mLastTimeUsed;
        }

        public TextView getPackageName() {
            return mPackageName;
        }

        public ImageView getAppIcon() {
            return mAppIcon;
        }

        public Switch getDisturb() {
            return isDisturb;
        }
    }
    public UsageListAdapterWithSwitch() {
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.usage_row, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        final Context context= viewHolder.getPackageName().getContext();
        long TimeInforground = 500 ;
        int minutes=500,seconds=500,hours=500 ;
        viewHolder.getPackageName().setText(getAppNameFromPackage(
                mCustomUsageStatsList.get(position).usageStats.getPackageName(), context));
        TimeInforground = mCustomUsageStatsList.get(position).usageStats.getTotalTimeInForeground();

        minutes = (int) ((TimeInforground / (1000 * 60)) % 60);

        seconds = (int) (TimeInforground / 1000) % 60;

        hours = (int) ((TimeInforground / (1000 * 60 * 60)) % 24);
        if(hours==0&&minutes==0){
            viewHolder.getLastTimeUsed().setText(seconds + "s");
            Useage = seconds + "s";
        }
        else if(hours==0){
            viewHolder.getLastTimeUsed().setText(minutes + "m" + seconds + "s");
            Useage = minutes + "m" + seconds + "s";
        }
        else {
            viewHolder.getLastTimeUsed().setText(hours + "h" + minutes + "m" + seconds + "s");
            Useage = hours + "h" + minutes + "m" + seconds + "s";
        }
        final  App app = new App();
        app.name = getAppNameFromPackage(mCustomUsageStatsList.get(position).usageStats.getPackageName(), context);
        app.usage =Useage;

        if(ApplicationDB.find(context,app.name)!=null){
            viewHolder.getDisturb().setChecked(ApplicationDB.find(context,app.name).monitorSwitch);
        }
        else{
            app.monitorSwitch=false;
            applicationDB.insert(context,app);
        }
        viewHolder.getDisturb().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b==true) {
                    app.monitorSwitch = true;
                    ApplicationDB.update(context, app);
                    Toast.makeText(context, "Disturb"+app.name, Toast.LENGTH_SHORT).show();
                }
                else {
                    app.monitorSwitch = false;
                    ApplicationDB.update(context, app);
                    Toast.makeText(context, "Not Disturb"+app.name, Toast.LENGTH_SHORT).show();
                }
            }
        });
        viewHolder.getAppIcon().setImageDrawable(mCustomUsageStatsList.get(position).appIcon);
    }

    @Override
    public int getItemCount() {
        return mCustomUsageStatsList.size();
    }

    public void setCustomUsageStatsList(List<CustomUsageStats> customUsageStats) {
        mCustomUsageStatsList = customUsageStats;
    }

    private String getAppNameFromPackage(String packageName, Context context) {
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> pkgAppsList = context.getPackageManager()
                .queryIntentActivities(mainIntent, 0);
        for (ResolveInfo app : pkgAppsList) {
            if (app.activityInfo.packageName.equals(packageName)) {
                return app.activityInfo.loadLabel(context.getPackageManager()).toString();
            }
        }
        return null;
    }
}

