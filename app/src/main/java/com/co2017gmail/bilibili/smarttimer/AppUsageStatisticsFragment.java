package com.co2017gmail.bilibili.smarttimer;

import android.app.Application;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.pm.ResolveInfo;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;
import android.app.Fragment;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AppOpsManager;
import android.content.Context;
import android.os.Process;

import java.util.List;
import java.util.concurrent.TimeUnit;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;


import static android.app.AppOpsManager.MODE_ALLOWED;
import static android.app.AppOpsManager.OPSTR_GET_USAGE_STATS;


public class AppUsageStatisticsFragment extends Fragment {
    //NEW
    private static final String TAG1 = "AppUsageStatisticsFragm";
    private static final String TAG = AppUsageStatisticsFragment.class.getSimpleName();
    private static final int MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS = 100;
    ApplicationDB applicationDB;

    //VisibleForTesting
    UsageStatsManager mUsageStatsManager;
    UsageListAdapterWithSwitch mUsageListAdapter;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    Button mOpenUsageSettingButton;

    public static AppUsageStatisticsFragment newInstance() {
        AppUsageStatisticsFragment fragment = new AppUsageStatisticsFragment();
        return fragment;
    }

    public AppUsageStatisticsFragment() {
        //Required an empty constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //noinspection ResourceType
        mUsageStatsManager = (UsageStatsManager) getActivity()
                .getSystemService("usagestats"); //Context.USAGE_STATS_SERVICE

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_app_usage_statistics, container, false);
    }


    @Override
    public void onViewCreated(View rootView, Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);

        mUsageListAdapter = new UsageListAdapterWithSwitch();
        mRecyclerView = rootView.findViewById(R.id.recyclerview_app_usage);
        mLayoutManager = mRecyclerView.getLayoutManager();
        mRecyclerView.scrollToPosition(0);
        mRecyclerView.setAdapter(mUsageListAdapter);

        List<UsageStats> usageStatsList =
                getUsageStatistics(UsageStatsManager.INTERVAL_DAILY);
        Collections.sort(usageStatsList, new LastTimeLaunchedComparatorDesc());

    }


////////////////////////////******USER PERMISSION*****////////////////////////////

    private boolean hasPermission() {
        AppOpsManager appOps = (AppOpsManager)getActivity()//not original
                .getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(),getActivity().getPackageName());
        return mode == AppOpsManager.MODE_ALLOWED;
//        return ContextCompat.checkSelfPermission(this,
//                Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED;
    }

//
 ////////////////////////////******GET STATS*****////////////////////////////

    public List<UsageStats> getUsageStatistics(int intervalType) {
        // Get the app statistics since one year ago from the current time.

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -1);

        List<UsageStats> queryUsageStats = mUsageStatsManager
                .queryUsageStats(UsageStatsManager.INTERVAL_DAILY, System.currentTimeMillis()-1000*10, //.queryUsageStats(intervalType, cal.getTimeInMillis(),
                        System.currentTimeMillis());
            if (queryUsageStats.size() == 0) {
                Log.i(TAG, "The user may not allow the access to apps usage. ");
                Toast.makeText(getActivity(),
                        getString(R.string.explanation_access_to_appusage_is_not_enabled),
                        Toast.LENGTH_LONG).show();
                mOpenUsageSettingButton.setVisibility(View.VISIBLE);
                mOpenUsageSettingButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivityForResult(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS), MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS);
                        //startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
                    }
                });
            }
        updateAppsList(queryUsageStats);


        return queryUsageStats;
    }

    void updateAppsList(List<UsageStats> usageStatsList) {
        List<CustomUsageStats> customUsageStatsList = new ArrayList<>();

        for (int i = 0; i < usageStatsList.size(); i++) {
            CustomUsageStats customUsageStats = new CustomUsageStats();
            customUsageStats.usageStats = usageStatsList.get(i);
            Context context= getActivity();
            String appname = getAppNameFromPackage(customUsageStats.usageStats.getPackageName(),context);

            if(customUsageStats.usageStats.getLastTimeUsed()>970725976000L&&appname!=null) {
                try {
                    Drawable appIcon = getActivity().getPackageManager()
                            .getApplicationIcon(customUsageStats.usageStats.getPackageName());
                    customUsageStats.appIcon = appIcon;
                } catch (PackageManager.NameNotFoundException e) {
                    Log.w(TAG, String.format("App Icon is not found for %s",
                            customUsageStats.usageStats.getPackageName()));
                    customUsageStats.appIcon = getActivity()
                            .getDrawable(R.drawable.ic_default_app_launcher);
                }
                customUsageStatsList.add(customUsageStats);
            }
        }
        Collections.sort(customUsageStatsList, new customUsageStatsListComparator());

        mUsageListAdapter.setCustomUsageStatsList(customUsageStatsList);
        mUsageListAdapter.notifyDataSetChanged();
        mRecyclerView.scrollToPosition(0);
    }

    private static class LastTimeLaunchedComparatorDesc implements Comparator<UsageStats> {

        @Override
        public int compare(UsageStats left, UsageStats right) {
            return Long.compare(right.getTotalTimeInForeground(), left.getTotalTimeInForeground());
        }
    }

    private static class customUsageStatsListComparator implements Comparator<CustomUsageStats> {
        @Override
        public int compare(CustomUsageStats left, CustomUsageStats right) {
            return Long.compare(right.usageStats.getTotalTimeInForeground(), left.usageStats.getTotalTimeInForeground());
        }
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

    protected  String toUsageTime(UsageStats usageStats){
        long TimeInforground = usageStats.getTotalTimeInForeground() ;
        int minutes=500,seconds=500,hours=500 ;
        minutes = (int) ((TimeInforground / (1000 * 60)) % 60);

        seconds = (int) (TimeInforground / 1000) % 60;

        hours = (int) ((TimeInforground / (1000 * 60 * 60)) % 24);
        if(hours==0&&minutes==0){
            return seconds + "s";
        }
        else if(hours==0){
            return minutes + "m" + seconds + "s";
        }
        else {
            return hours + "h" + minutes + "m" + seconds + "s";
        }
    }

}
